/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PACKAGE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PRIVATE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodVisibilityIncreasedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodVisibilityReducedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class MethodVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodVisibility");
  }

  @Test
  public void detectsProtectedToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PROTECTED, PUBLIC);
    doBrokenApiTest("detectsProtectedToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @Test
  public void detectsPackageToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PACKAGE, PUBLIC);
    doBrokenApiTest("detectsPackageToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @Test
  public void detectsPackageToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PACKAGE, PROTECTED);
    doBrokenApiTest("detectsPackageToProtectedMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @Test
  public void detectsPrivateToPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PRIVATE, PUBLIC);
    doBrokenApiTest("detectsPrivateToPublicMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @Test
  public void detectsPrivateToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityIncreasedError = getMethodVisibilityIncreasedError(PRIVATE, PROTECTED);
    doBrokenApiTest("detectsPrivateToProtectedMethodOnExportedPublicClass", methodVisibilityIncreasedError);
  }

  @Test
  public void ignoresPrivateToPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPrivateToPackageMethodOnExportedPublicClass");
  }

  @Test
  public void detectsPublicToProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void detectsPublicToPackageMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPackageMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageMethodOnInternalPublicClass");
  }

  @Test
  public void detectsPublicToPrivateMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PUBLIC, PRIVATE);

    doBrokenApiTest("detectsPublicToPrivateMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPrivateMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateMethodOnInternalPublicClass");
  }

  @Test
  public void detectsProtectedToPackageMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PROTECTED, PACKAGE);

    doBrokenApiTest("detectsProtectedToPackageMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPackageMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageMethodOnInternalPublicClass");
  }

  @Test
  public void detectsProtectedToPrivateMethodOnExportedPublicClass() throws Exception {
    String[] methodVisibilityReducedError = getMethodVisibilityReducedError(PROTECTED, PRIVATE);

    doBrokenApiTest("detectsProtectedToPrivateMethodOnExportedPublicClass", methodVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPrivateMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresPackageToPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateMethodOnExportedPublicClass");
  }
}
