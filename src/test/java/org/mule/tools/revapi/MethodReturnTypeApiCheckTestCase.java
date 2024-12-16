/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.DO_STUFF_METHOD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getReturnTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodReturnTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodReturnTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodReturnType");
  }

  @MavenPluginTest
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicClass", returnTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsChangedReturnTypeInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInProtectedMethodOnExportedPublicClass", returnTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoExtendClass");
  }

  @MavenPluginTest
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicNoExtendClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicNoExtendClass", returnTypeChangedError);
  }

  @MavenPluginTest
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicNoInstantiateClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicNoInstantiateClass", returnTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoInstantiateClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoInstantiateClass");
  }
}
