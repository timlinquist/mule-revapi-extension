/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PACKAGE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PRIVATE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodVisibilityIncreasedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodVisibilityReducedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodVisibility", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsProtectedToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PROTECTED, PUBLIC);
    doBrokenApiTest("detectsProtectedToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void detectsPackageToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PACKAGE, PUBLIC);
    doBrokenApiTest("detectsPackageToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void detectsPackageToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PACKAGE, PROTECTED);
    doBrokenApiTest("detectsPackageToProtectedMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void detectsPrivateToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PRIVATE, PUBLIC);
    doBrokenApiTest("detectsPrivateToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void detectsPrivateToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PRIVATE, PROTECTED);
    doBrokenApiTest("detectsPrivateToProtectedMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void ignoresPrivateToPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPrivateToPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void detectsPublicToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsPublicToPackageMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPackageMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsPublicToPrivateMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PRIVATE);

    doBrokenApiTest("detectsPublicToPrivateMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPrivateMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPackageMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PROTECTED, PACKAGE);

    doBrokenApiTest("detectsProtectedToPackageMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPackageMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPrivateMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PROTECTED, PRIVATE);

    doBrokenApiTest("detectsProtectedToPrivateMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPrivateMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresPackageToPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateMethodOnExportedPublicClass");
  }
}
