/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static java.io.File.separator;
import static java.lang.System.getProperty;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mule.tools.revapi.ApiErrorLogUtils.API_ERROR_JUSTIFICATION;

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

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(MavenJUnitTestRunner.class)
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

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;
  private final String folder;

  public AbstractApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, String folder) throws Exception {
    this.mavenRuntime =
        builder.withCliOptions("--batch-mode", "-Dmaven.repo.local=" + getProperty("maven.repo.local", "")).build();
    this.folder = folder;
  }

  protected void doBrokenApiTest(String projectName, String[]... brokenApiLog) throws Exception {
    MavenExecutionResult result = runMaven(projectName);

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
    MavenExecutionResult result = runMaven(projectName);

    List<String> logLines = getLogLines(result);

    Map<String, List<String>> moduleLogs = splitLog(logLines);

    List<String> reactorSummaryLog = moduleLogs.get(REACTOR_SUMMARY);
    List<String> revapiCheckLog = moduleLogs.get(REVAPI_CHECK);
    // if there was an unexpected error, it will be in the revapi output
    assertThat(revapiCheckLog.toString(), reactorSummaryLog, not(hasItem(containsString(API_ERROR_FOUND))));
    assertThat(reactorSummaryLog, not(hasItem(containsString(MAVEN_BUILD_ERROR))));
  }

  private MavenExecutionResult runMaven(String projectName) throws Exception {
    // To reuse a parent pom, it has to be manually copied to the expected folder
    Path parentPomFile = Paths.get(getProperty("user.dir"), "src/test/projects/parent/pom.xml");
    Path parentProjectFolder = new File(resources.getBasedir().getParent(), "parent").toPath();
    copy(parentPomFile, Paths.get(createDirectories(parentProjectFolder).toString(), "pom.xml"), REPLACE_EXISTING);

    File basedir = resources.getBasedir(folder + separator + projectName);
    return mavenRuntime.forProject(basedir).execute("install");
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
}
