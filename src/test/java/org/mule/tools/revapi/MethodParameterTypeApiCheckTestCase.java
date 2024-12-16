/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.DO_STUFF_METHOD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getParameterTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodParameterTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodParameterTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodParameterType", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsTypeParameterChangeInPublicMethodOnExportedPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedPublicClass", parameterTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsTypeParameterChangeInProtectedMethodOnExternalPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInProtectedMethodOnExternalPublicClass", parameterTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInPackageMethodOnExternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPackageMethodOnExternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInPrivatedMethodOnExternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPrivatedMethodOnExternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInProtectedMethodOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsTypeParameterChangeInPublicMethodOnExportedNoExtendPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedNoExtendPublicClass", parameterTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInProtectedMethodOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnExportedNoInstantiatePublicClass");
  }


  @MavenPluginTest
  public void detectsTypeParameterChangeInPublicMethodOnExportedNoInstantiatePublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedNoInstantiatePublicClass", parameterTypeChangedError);
  }
}
