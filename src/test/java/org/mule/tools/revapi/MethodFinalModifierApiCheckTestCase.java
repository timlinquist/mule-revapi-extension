/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNowFinalError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodFinalModifier", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedFinalModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowFinalError = getMethodNowFinalError(PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInPublicMethodOnExportedPublicClass", methodNowFinalError);
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedFinalModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowFinalError = getMethodNowFinalError(PROTECTED, PROTECTED_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInProtectedMethodOnExportedPublicClass", methodNowFinalError);
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPrivateMethodOnExportedPackageClass");
  }
}
