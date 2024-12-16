/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
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
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class ClassVisibilityApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassVisibilityApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "classVisibility", isPromotedApi);
  }

  @MavenPluginTest
  @Disabled("Revapi incorrectly reports this case as if the class was removed instead of its visibility reduced. " +
      "The following issue has been reported in the revapi project: https://github.com/revapi/revapi/issues/288")
  public void detectsPublicToPackageExportedClass() throws Exception {
    // Note: this kind of change was detected as a visibility reduction, but is now considered by revapi to be a removal of the
    // class
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A, PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageExportedClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPackageInternalClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageInternalClass");
  }

  @MavenPluginTest
  public void detectsPublicToProtectedExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PROTECTED);
    doBrokenApiTest("detectsPublicToProtectedExportedInnerClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToProtectedInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToProtectedInternalInnerClass");
  }

  @MavenPluginTest
  public void detectsPublicToPackageExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PACKAGE);
    doBrokenApiTest("detectsPublicToPackageExportedInnerClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPackageInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPackageInternalInnerClass");
  }

  @MavenPluginTest
  public void detectsPublicToPrivateExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PUBLIC, PRIVATE);
    doBrokenApiTest("detectsPublicToPrivateExportedInnerClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresPublicToPrivateInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPublicToPrivateInternalInnerClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPackageExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PROTECTED, PACKAGE);
    doBrokenApiTest("detectsProtectedToPackageExportedInnerClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPackageInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPackageInternalInnerClass");
  }

  @MavenPluginTest
  public void detectsProtectedToPrivateExportedInnerClass() throws Exception {
    String[] classVisibilityReducedError = getClassVisibilityReducedError(ORG_FOO_A_B, PROTECTED, PRIVATE);
    doBrokenApiTest("detectsProtectedToPrivateExportedInnerClass", classVisibilityReducedError);
  }

  @MavenPluginTest
  public void ignoresProtectedToPrivateInternalInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresProtectedToPrivateInternalInnerClass");
  }

  @MavenPluginTest
  public void ignoresPackageToPrivateExportedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresPackageToPrivateExportedInnerClass");
  }

}
