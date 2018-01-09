/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
}
