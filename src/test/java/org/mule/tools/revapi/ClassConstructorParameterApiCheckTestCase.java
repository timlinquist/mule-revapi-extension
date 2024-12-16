/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.CONSTRUCTOR_METHOD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.STRING;
import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorNumberOfParametersChangedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getParameterTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class ClassConstructorParameterApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassConstructorParameterApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi)
      throws Exception {
    super(builder, "classConstructorParameter", isPromotedApi);
  }

  @MavenPluginTest
  public void ignoresAddedParameterToProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedParameterToPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] numberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A, STRING, STRING + ", " + STRING);
    doBrokenApiTest("detectsAddedParameterToPublicConstructorOnExportedNoExtendPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresAddedParameterToPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterToProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedParameterToPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] numberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A, STRING + ", " + STRING, STRING);
    doBrokenApiTest("detectsRemovedParameterToPublicConstructorOnExportedNoExtendPublicClass", numberOfParametersChangedError);
  }

  @MavenPluginTest
  public void ignoresRemovedParameterToPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInProtectedConstructorExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedConstructorExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsTypeParameterChangeInPublicConstructorExportedNoExtendPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, CONSTRUCTOR_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicConstructorExportedNoExtendPublicClass", parameterTypeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInPublicConstructorExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPublicConstructorExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresTypeParameterChangeInProtectedConstructorExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedConstructorExportedNoInstantiatePublicClass");
  }
}
