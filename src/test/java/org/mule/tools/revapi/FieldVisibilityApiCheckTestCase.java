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
import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldVisibilityReducedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class FieldVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "fieldVisibility");
  }

  @Test
  public void detectsPublicToProtectedFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToProtectedFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedFieldOnInternalPublicClass");
  }

  @Test
  public void detectsPublicToPackageFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPackageFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageFieldOnInternalPublicClass");
  }

  @Test
  public void detectsPublicToPrivateFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PRIVATE);
    doBrokenApiTest("detectsPublicToPrivateFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @Test
  public void ignoresPublicToPrivateFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateFieldOnInternalPublicClass");
  }

  @Test
  public void detectsProtectedToPackageFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PROTECTED, PACKAGE);
    doBrokenApiTest("detectsProtectedToPackageFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPackageFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageFieldOnInternalPublicClass");
  }

  @Test
  public void detectsProtectedToPrivateFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PROTECTED, PRIVATE);
    doBrokenApiTest("detectsProtectedToPrivateFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @Test
  public void ignoresProtectedToPrivateFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateFieldOnInternalPublicClass");
  }

  @Test
  public void ignoresPackageToPrivateFieldOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateFieldOnExportedPublicClass");
  }
}
