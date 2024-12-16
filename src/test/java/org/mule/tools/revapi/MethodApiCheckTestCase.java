/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodAddedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodRemovedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "method", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsAddedPublicMethodOnExportedPublicClass", methodAddedError);
  }

  @MavenPluginTest
  public void ignoresAddedPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsAddedProtectedMethodOnExportedPublicClass", methodAddedError);
  }

  @MavenPluginTest
  public void ignoresAddedProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void detectsRemovedPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedPublicClass", methodRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedProtectedMethodOnExportedPublicClass", methodRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedMethodOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedMethodOnExportedPublicClassExtendingNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedPublicClassExtendingNoExtendClass");
  }

  @MavenPluginTest
  public void detectsRemovedPublicMethodOnExportedNoExtendPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedNoExtendPublicClass", methodRemovedError);
  }

  @MavenPluginTest
  public void detectsRemovedPublicMethodOnExportedNoInstantiatePublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedNoInstantiatePublicClass", methodRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedMethodOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedNoInstantiatePublicClass");
  }
}
