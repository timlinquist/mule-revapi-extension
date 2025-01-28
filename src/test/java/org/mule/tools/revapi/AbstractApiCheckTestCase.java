/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.API_ERROR_JUSTIFICATION;

import static java.io.File.separator;
import static java.lang.System.getProperty;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsIterableContaining.hasItem;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import io.takari.maven.testing.TestResources5;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public abstract class AbstractApiCheckTestCase {

  private static final Pattern moduleTitleStart = Pattern.compile("\\[INFO]\\s-+<.*>-+");
  private static final Pattern reactorSummaryStart = Pattern.compile("\\[INFO]\\s-+");
  private static final Pattern revapiCheckStart =
      Pattern.compile("\\[INFO]\\sComparing\\s\\[.+]\\sagainst\\s\\[.+].*");
  private static final String MAVEN_BUILD_PREFIX = "[INFO] Building ";
  private static final String API_ERROR_FOUND = "The following API problems caused the build to fail:";
  private static final String REACTOR_SUMMARY = "Reactor Summary";
  private static final String MAVEN_BUILD_ERROR = "[INFO] BUILD FAILURE";
  private static final String REVAPI_CHECK = "Revapi check";

  @RegisterExtension
  final TestResources5 resources = new TestResources5();

  private final MavenRuntime mavenRuntime;
  private String folder;
  private boolean isPromotedApi = false;

  public AbstractApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, String folder) throws Exception {
    this(builder, folder, "17");
  }

  public AbstractApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, String folder, boolean isPromotedApi)
      throws Exception {
    this(builder, folder, "17");
    this.isPromotedApi = isPromotedApi;
  }

  public AbstractApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, String folder, String sourceLevel) throws Exception {
    this.mavenRuntime = builder.withCliOptions("--batch-mode",
                                               "-Dmaven.repo.local=" + getProperty("maven.repo.local", ""),
                                               "-Dmaven.compiler.source=" + sourceLevel,
                                               "-Dmaven.compiler.target=" + sourceLevel,
                                               "-Dmule.revapi.moduleSystem.mode=MULE")
        .build();
    this.folder = folder;
  }

  protected void doBrokenApiTest(String projectName, String[]... brokenApiLog) throws Exception {
    MavenExecutionResult result = validateMavenProject(projectName, isPromotedApi);

    List<String> logLines = getLogLines(result);

    Map<String, List<String>> moduleLogs = splitLog(logLines);

    assertThat(moduleLogs.get(REACTOR_SUMMARY), hasItem(containsString(API_ERROR_FOUND)));

    List<String> revapiCheckLog = moduleLogs.get(REVAPI_CHECK);
    for (String[] apiError : brokenApiLog) {
      assertMultiLogLine(revapiCheckLog, apiError);
    }

    int errorCount = (int) revapiCheckLog.stream().filter(s -> s.contains(API_ERROR_JUSTIFICATION)).count();
    assertThat(errorCount, equalTo(brokenApiLog.length));
  }

  protected void doUnmodifiedApiTest(String projectName) throws Exception {
    MavenExecutionResult result = validateMavenProject(projectName, isPromotedApi);

    List<String> logLines = getLogLines(result);

    Map<String, List<String>> moduleLogs = splitLog(logLines);

    List<String> reactorSummaryLog = moduleLogs.get(REACTOR_SUMMARY);
    List<String> revapiCheckLog = moduleLogs.get(REVAPI_CHECK);
    // if there was an unexpected error, it will be in the revapi output
    assertThat(revapiCheckLog.toString(), reactorSummaryLog, not(hasItem(containsString(API_ERROR_FOUND))));
    assertThat(reactorSummaryLog, not(hasItem(containsString(MAVEN_BUILD_ERROR))));
  }

  /**
   * Executes maven, which will include an API check that takes into account if the project API should be validated as a
   * standalone API or as a dependency of another API. In case of being validated as a dependency, a non-breaking API will be used
   * as the main one.
   * 
   * @param projectName   The name of the project to validate.
   * @param isPromotedApi True when the API should be validated as a dependency.
   * @return The {@link MavenExecutionResult}.
   * @throws Exception In case of maven fatal errors.
   */
  private MavenExecutionResult validateMavenProject(String projectName, boolean isPromotedApi) throws Exception {
    MavenExecutionResult result;
    if (isPromotedApi) {
      // We need both API versions to be built. The we can use them as dependencies that should be promoted.
      result = runMaven(projectName, "-Drevapi.skip=true");
      // We do not need an intermediate folder: The promotedApi project is the same one for all the tests.
      this.folder = "";
      // This time we do the validation.
      result = runMaven("promotedApi", isNewArtifactSnapshotVersion(result) ? "-DnewDependencyVersion=1.0.1-SNAPSHOT"
          : "-DnewDependencyVersion=1.0.1");
    } else {
      result = runMaven(projectName);
    }
    return result;
  }


  private boolean isNewArtifactSnapshotVersion(MavenExecutionResult result) {
    return result.getLog().stream().anyMatch(s -> s.contains("Building Foo Module 1.0.1-SNAPSHOT"));
  }

  private MavenExecutionResult runMaven(String projectName, String... additionalCliOptions) throws Exception {
    // To reuse a parent pom, it has to be manually copied to the expected folder
    Path parentPomFile = Paths.get(getProperty("user.dir"), "src/test/projects/parent/pom.xml");
    Path parentProjectFolder = new File(resources.getBasedir().getParent(), "parent").toPath();
    copy(parentPomFile, Paths.get(createDirectories(parentProjectFolder).toString(), "pom.xml"), REPLACE_EXISTING);

    File basedir = resources.getBasedir(folder + separator + projectName);
    return mavenRuntime.forProject(basedir).withCliOptions(additionalCliOptions).execute("install");
  }

  private Map<String, List<String>> splitLog(List<String> logLines) {
    Map<String, List<String>> result = new HashMap<>();

    int i = 0;
    while (i < logLines.size()) {
      String currentLine = logLines.get(i);
      if (moduleTitleStart.matcher(currentLine).find() || revapiCheckStart.matcher(currentLine).find()
          || reactorSummaryStart.matcher(currentLine).find()) {
        if (i + 1 < logLines.size() && logLines.get(i + 1).startsWith(MAVEN_BUILD_PREFIX)) {
          int moduleLogStart = i;
          String moduleName = logLines.get(i + 1).substring(MAVEN_BUILD_PREFIX.length());
          i = i + 3;

          while (i < logLines.size() && !(moduleTitleStart.matcher(logLines.get(i)).find())
              && !(revapiCheckStart.matcher(logLines.get(i)).find()) && !(reactorSummaryStart.matcher(logLines.get(i)).find())) {
            i++;
          }

          result.put(moduleName, logLines.subList(moduleLogStart, i));
        } else if (i + 1 < logLines.size() && revapiCheckStart.matcher(currentLine).find()) {
          int moduleLogStart = i;
          i = i + 1;

          while (i < logLines.size() && !(moduleTitleStart.matcher(logLines.get(i)).find())
              && !(reactorSummaryStart.matcher(logLines.get(i)).find())) {
            i++;
          }

          result.put(REVAPI_CHECK, logLines.subList(moduleLogStart, i));
        } else if (i + 1 < logLines.size() && logLines.get(i + 1).startsWith("[INFO] Reactor Summary:")) {
          int moduleLogStart = i;
          i = i + 3;

          while (i < logLines.size()) {
            i++;
          }

          result.put(REACTOR_SUMMARY, logLines.subList(moduleLogStart, i));
        } else {
          i++;
        }
      } else {
        i++;
      }
    }
    return result;
  }

  private static void assertMultiLogLine(List<String> log, String... lines) {
    if (lines.length < 2) {
      throw new IllegalArgumentException("There must be more than one line to assert. Use MavenExecutionResult#assertLogText instead");
    }

    Collection<String> linesCollection = new ArrayList<>();
    Collections.addAll(linesCollection, lines);

    int linesMatched = 0;

    for (int i = 0; i < log.size(); i++) {
      if (i + lines.length - linesMatched > log.size()) {
        break;
      }

      final String currentLine = log.get(i);

      if (linesCollection.stream().anyMatch(currentLine::endsWith)) {
        linesMatched++;
      }

      if (linesMatched == lines.length) {
        return;
      }
    }

    StringBuilder builder = new StringBuilder("Expected multi-line log: ");
    stream(lines).forEach(s -> builder.append("\n").append(s));
    builder.append("\nbut was");
    log.stream().forEach(s -> builder.append(s).append("\n"));

    throw new AssertionError(builder.toString());
  }

  private List<String> getLogLines(MavenExecutionResult result) {
    List<String> log;
    try {
      Field logField = result.getClass().getDeclaredField("log");
      logField.setAccessible(true);
      log = (List<String>) logField.get(result);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    return log;
  }

  public static class ApiCheckTestCaseContextProvider
      implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
      return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
                                                                                       ExtensionContext context) {

      return Stream.of(invocationContext(false), invocationContext(true));
    }

    private TestTemplateInvocationContext invocationContext(boolean parameter) {
      return new TestTemplateInvocationContext() {

        @Override
        public String getDisplayName(int invocationIndex) {
          return "isPromotedAPI: " + parameter;
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
          return Collections.singletonList(new ParameterResolver() {

            @Override
            public boolean supportsParameter(ParameterContext parameterContext,
                                             ExtensionContext extensionContext) {
              return parameterContext.getParameter().getType().equals(boolean.class);
            }

            @Override
            public Object resolveParameter(ParameterContext parameterContext,
                                           ExtensionContext extensionContext) {
              return parameter;
            }
          });
        }
      };
    }
  }
}
