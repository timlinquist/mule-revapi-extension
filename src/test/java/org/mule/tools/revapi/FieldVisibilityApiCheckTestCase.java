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
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class FieldVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "fieldVisibility", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsPublicToProtectedFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToProtectedFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsPublicToPackageFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPackageFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsPublicToPrivateFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PUBLIC, PRIVATE);
    doBrokenApiTest("detectsPublicToPrivateFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPrivateFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPackageFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PROTECTED, PACKAGE);
    doBrokenApiTest("detectsProtectedToPackageFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPackageFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPrivateFieldOnExportedPublicClass() throws Exception {
    String[] fieldVisibilityReducedError = getFieldVisibilityReducedError(PROTECTED, PRIVATE);
    doBrokenApiTest("detectsProtectedToPrivateFieldOnExportedPublicClass", fieldVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPrivateFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresPackageToPrivateFieldOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateFieldOnExportedPublicClass");
  }
}
