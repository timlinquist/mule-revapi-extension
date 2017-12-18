/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.PACKAGE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PRIVATE;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.getClassVisibilityReducedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class ClassVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "classVisibility");
  }

  @Test
  public void detectsPublicToPackageExportedClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A, PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageExportedClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPackageInternalClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageInternalClass");
  }

  @Test
  public void detectsPublicToProtectedExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedExportedInnerClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToProtectedInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedInternalInnerClass");
  }

  @Test
  public void detectsPublicToPackageExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageExportedInnerClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPackageInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageInternalInnerClass");
  }

  @Test
  public void detectsPublicToPrivateExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PRIVATE);
    doBrokenApiTest("detectsPublicToPrivateExportedInnerClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPrivateInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateInternalInnerClass");
  }

  @Test
  public void detectsProtectedToPackageExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PROTECTED, PACKAGE);
    doBrokenApiTest("detectsProtectedToPackageExportedInnerClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPackageInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageInternalInnerClass");
  }

  @Test
  public void detectsProtectedToPrivateExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PROTECTED, PRIVATE);
    doBrokenApiTest("detectsProtectedToPrivateExportedInnerClass", classVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPrivateInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateInternalInnerClass");
  }

  @Test
  public void ignoresPackageToPrivateExportedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateExportedInnerClass");
  }

}
