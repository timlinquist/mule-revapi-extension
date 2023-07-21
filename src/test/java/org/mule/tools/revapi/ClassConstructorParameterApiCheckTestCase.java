/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.CONSTRUCTOR_METHOD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.STRING;
import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorNumberOfParametersChangedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getParameterTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class ClassConstructorParameterApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassConstructorParameterApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "classConstructorParameter");
  }

  @Test
  public void ignoresAddedParameterToProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @Test
  public void detectsAddedParameterToPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] numberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A, STRING, STRING + ", " + STRING);
    doBrokenApiTest("detectsAddedParameterToPublicConstructorOnExportedNoExtendPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresAddedParameterToPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresRemovedParameterToProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @Test
  public void detectsRemovedParameterToPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] numberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A, STRING + ", " + STRING, STRING);
    doBrokenApiTest("detectsRemovedParameterToPublicConstructorOnExportedNoExtendPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresRemovedParameterToPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresTypeParameterChangeInProtectedConstructorExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedConstructorExportedNoExtendPublicClass");
  }

  @Test
  public void detectsTypeParameterChangeInPublicConstructorExportedNoExtendPublicClass() throws Exception {
    String[] parameterTypeChangedError = getParameterTypeChangedError(ORG_FOO_A, CONSTRUCTOR_METHOD);
    doBrokenApiTest("detectsTypeParameterChangeInPublicConstructorExportedNoExtendPublicClass", parameterTypeChangedError);
  }

  @Test
  public void ignoresTypeParameterChangeInPublicConstructorExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInPublicConstructorExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresAddedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresRemovedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterToProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresTypeParameterChangeInProtectedConstructorExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeParameterChangeInProtectedConstructorExportedNoInstantiatePublicClass");
  }
}
