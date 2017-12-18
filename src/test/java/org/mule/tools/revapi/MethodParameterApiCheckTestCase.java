/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.EMPTY_PARAMS;
import static org.mule.tools.revapi.ApiErrorLogUtils.STRING;
import static org.mule.tools.revapi.ApiErrorLogUtils.getNumberOfParametersChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class MethodParameterApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodParameterApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodParameter");
  }

  @Test
  public void detectsAddedParameterInPublicMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInPublicMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresAddedParameterInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsAddedParameterInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsAddedParameterInProtectedMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresAddedParameterInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresAddedParameterInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedParameterInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedParameterInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedParameterInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedParameterInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedParameterInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedParameterInPrivateMethodOnExportedPackageClass");
  }

  @Test
  public void detectsRemovedParameterInPublicMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);

    doBrokenApiTest("detectsRemovedParameterInPublicMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresRemovedParameterInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsRemovedParameterInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] numberOfParametersChangedError = getNumberOfParametersChangedError(STRING, EMPTY_PARAMS);
    doBrokenApiTest("detectsRemovedParameterInProtectedMethodOnExportedPublicClass", numberOfParametersChangedError);
  }

  @Test
  public void ignoresRemovedParameterInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresRemovedParameterInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedParameterInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedParameterInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedParameterInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedParameterInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedParameterInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedParameterInPrivateMethodOnExportedPackageClass");
  }
}
