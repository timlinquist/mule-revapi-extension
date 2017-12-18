/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static java.io.File.separator;
import static java.lang.System.getProperty;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mule.tools.revapi.ApiErrorLogUtils.API_ERROR_JUSTIFICATION;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(MavenJUnitTestRunner.class)
public abstract class AbstractApiCheckTestCase {

  private static final String INFO_LOG_PREFIX = "[INFO] ";
  private static final String sectionSeparator = INFO_LOG_PREFIX +
      "------------------------------------------------------------------------";
  private static final String MAVEN_BUILD_PREFIX = "[INFO] Building ";
  private static final String API_ERROR_FOUND = "The following API problems caused the build to fail:";
  private static final String REACTOR_SUMMARY = "Reactor Summary";
  private static final String MAVEN_BUILD_ERROR = "[INFO] BUILD FAILURE";

  @Rule
  public final TestResources resources = new TestResources();

  private final MavenRuntime mavenRuntime;
  private final String folder;

  public AbstractApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, String folder) throws Exception {
    this.mavenRuntime = builder.build();
    this.folder = folder;
  }

  protected void doBrokenApiTest(String projectName, String[]... brokenApiLog) throws Exception {
    MavenExecutionResult result = runMaven(projectName);

    List<String> logLines = getLogLines(result);

    Map<String, List<String>> moduleLogs = splitLog(logLines);

    List<String> reactorSummaryLog = moduleLogs.get(REACTOR_SUMMARY);
    assertThat(reactorSummaryLog, hasItem(containsString(API_ERROR_FOUND)));
    for (String[] apiError : brokenApiLog) {
      assertMultiLogLine(reactorSummaryLog, apiError);
    }

    int errorCount = reactorSummaryLog.stream().filter(s -> s.equals(API_ERROR_JUSTIFICATION)).collect(toList()).size();
    assertThat(errorCount, equalTo(brokenApiLog.length));
  }

  protected void doUnmodifiedApiTest(String projectName) throws Exception {
    MavenExecutionResult result = runMaven(projectName);

    List<String> logLines = getLogLines(result);

    Map<String, List<String>> moduleLogs = splitLog(logLines);

    List<String> reactorSummaryLog = moduleLogs.get(REACTOR_SUMMARY);
    assertThat(reactorSummaryLog, not(hasItem(containsString(API_ERROR_FOUND))));
    assertThat(reactorSummaryLog, not(hasItem(containsString(MAVEN_BUILD_ERROR))));
  }

  private MavenExecutionResult runMaven(String projectName) throws Exception {
    // To reuse a parent pom, it has to be manually copied to the expected folder
    File parentPomFile = Paths.get(getProperty("user.dir"), "src/test/projects/parent/pom.xml").toFile();
    File parentProjectFolder = new File(resources.getBasedir().getParent(), "parent");
    copyFile(parentPomFile, new File(parentProjectFolder, "pom.xml"));

    File basedir = resources.getBasedir(folder + separator + projectName);
    return mavenRuntime.forProject(basedir).execute("install");
  }

  private Map<String, List<String>> splitLog(List<String> logLines) {
    Map<String, List<String>> result = new HashMap<>();

    int i = 0;
    while (i < logLines.size()) {
      String currentLine = logLines.get(i);
      if (sectionSeparator.equals(currentLine)) {
        if (i + 1 < logLines.size() && logLines.get(i + 1).startsWith(MAVEN_BUILD_PREFIX)) {
          int moduleLogStart = i;
          String moduleName = logLines.get(i + 1).substring(MAVEN_BUILD_PREFIX.length());
          i = i + 3;

          while (i < logLines.size() && !(logLines.get(i).equals(sectionSeparator))) {
            i++;
          }

          result.put(moduleName, logLines.subList(moduleLogStart, i));
        } else if (i + 1 < logLines.size() && logLines.get(i + 1).startsWith("[INFO] Reactor Summary:")) {
          int moduleLogStart = i;
          String moduleName = REACTOR_SUMMARY;
          i = i + 3;

          while (i < logLines.size()) {
            i++;
          }

          result.put(moduleName, logLines.subList(moduleLogStart, i));
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

    for (int i = 0; i < log.size(); i++) {
      if (i + lines.length > log.size()) {
        break;
      }
      int j = 0;
      while (j < lines.length && log.get(i + j).equals(lines[j])) {
        j++;
      }

      if (j == lines.length) {
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
