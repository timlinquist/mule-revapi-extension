/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNoLongerStaticError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNowStaticError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodStaticModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodStaticModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodStaticModifier");
  }

  @MavenPluginTest
  public void detectsAddedStaticModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowStaticError = getMethodNowStaticError(PUBLIC, PUBLIC_STATIC);
    doBrokenApiTest("detectsAddedStaticModifierInPublicMethodOnExportedPublicClass", methodNowStaticError);
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedStaticModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowStaticError = getMethodNowStaticError(PROTECTED, PROTECTED_STATIC);
    doBrokenApiTest("detectsAddedStaticModifierInProtectedMethodOnExportedPublicClass", methodNowStaticError);
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void detectsRemovedStaticModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNoLongerStaticError = getMethodNoLongerStaticError(PUBLIC_STATIC, PUBLIC);
    doBrokenApiTest("detectsRemovedStaticModifierInPublicMethodOnExportedPublicClass", methodNoLongerStaticError);
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedStaticModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNoLongerStaticError = getMethodNoLongerStaticError(PROTECTED_STATIC, PROTECTED);
    doBrokenApiTest("detectsRemovedStaticModifierInProtectedMethodOnExportedPublicClass", methodNoLongerStaticError);
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedStaticModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPrivateMethodOnExportedPackageClass");
  }
}
