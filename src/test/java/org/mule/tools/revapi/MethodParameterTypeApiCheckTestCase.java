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
import org.junit.Test;

public class MethodParameterTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodParameterTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodParameterType");
  }

  @Test
  public void detectsTypeParameterChangeInPublicMethodOnExportedPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedPublicClass", parameterTypeChangedError);
  }

  @Test
  public void ignoresTypeParameterChangeInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsTypeParameterChangeInProtectedMethodOnExternalPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInProtectedMethodOnExternalPublicClass", parameterTypeChangedError);
  }

  @Test
  public void ignoresTypeParameterChangeInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresTypeParameterChangeInPackageMethodOnExternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPackageMethodOnExternalPublicClass");
  }

  @Test
  public void ignoresTypeParameterChangeInPrivatedMethodOnExternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPrivatedMethodOnExternalPublicClass");
  }

  @Test
  public void ignoresTypeParameterChangeInProtectedMethodOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnExportedNoExtendPublicClass");
  }

  @Test
  public void detectsTypeParameterChangeInPublicMethodOnExportedNoExtendPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedNoExtendPublicClass", parameterTypeChangedError);
  }

  @Test
  public void ignoresTypeParameterChangeInProtectedMethodOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedMethodOnExportedNoInstantiatePublicClass");
  }


  @Test
  public void detectsTypeParameterChangeInPublicMethodOnExportedNoInstantiatePublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicMethodOnExportedNoInstantiatePublicClass", parameterTypeChangedError);
  }
}
