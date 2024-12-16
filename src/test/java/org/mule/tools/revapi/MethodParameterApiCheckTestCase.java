/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.EMPTY_PARAMS;
import static org.mule.tools.revapi.ApiErrorLogUtils.STRING;
import static org.mule.tools.revapi.ApiErrorLogUtils.getNumberOfParametersChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodParameterApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodParameterApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodParameter", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedParameterInPublicMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInPublicMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedParameterInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInProtectedMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresAddedParameterInProtectedMethodOnExportedPublicNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnExportedPublicNoExtendClass");
  }

  @MavenPluginTest
  public void detectsAddedParameterInPublicMethodOnExportedPublicNoExtendClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInPublicMethodOnExportedPublicNoExtendClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresAddedParameterInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void detectsRemovedParameterInPublicMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);

    doBrokenApiTest("detectsRemovedParameterInPublicMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedParameterInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);
    doBrokenApiTest("detectsRemovedParameterInProtectedMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPrivateMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPackageMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPrivateMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInProtectedMethodOnExportedPublicNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnExportedPublicNoExtendClass");
  }

  @MavenPluginTest
  public void detectsRemovedParameterInPublicMethodOnExportedPublicNoExtendClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);
    doBrokenApiTest("detectsRemovedParameterInPublicMethodOnExportedPublicNoExtendClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void detectsAddedParameterInPublicMethodOnExportedPublicNoInstantiateClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInPublicMethodOnExportedPublicNoInstantiateClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void detectsRemovedParameterInPublicMethodOnExportedPublicNoInstantiateClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);
    doBrokenApiTest("detectsRemovedParameterInPublicMethodOnExportedPublicNoInstantiateClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresAddedParameterInProtectedMethodOnExportedPublicNoInstantiateClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnExportedPublicNoInstantiateClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterInProtectedMethodOnExportedPublicNoInstantiateClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnExportedPublicNoInstantiateClass");
  }
}
