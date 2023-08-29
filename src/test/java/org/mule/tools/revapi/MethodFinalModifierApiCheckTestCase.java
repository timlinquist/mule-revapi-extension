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
import org.junit.Test;

public class MethodFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodFinalModifier");
  }

  @Test
  public void detectsAddedFinalModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowFinalError = getMethodNowFinalError(PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInPublicMethodOnExportedPublicClass", methodNowFinalError);
  }

  @Test
  public void ignoresAddedFinalModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsAddedFinalModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowFinalError = getMethodNowFinalError(PROTECTED, PROTECTED_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInProtectedMethodOnExportedPublicClass", methodNowFinalError);
  }

  @Test
  public void ignoresAddedFinalModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresAddedFinalModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedFinalModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedFinalModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedFinalModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedFinalModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedFinalModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInPrivateMethodOnExportedPackageClass");
  }
}
