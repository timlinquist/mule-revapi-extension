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
import org.junit.Test;

public class MethodApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "method");
  }

  @Test
  public void detectsAddedPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsAddedPublicMethodOnExportedPublicClass", methodAddedError);
  }

  @Test
  public void ignoresAddedPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsAddedProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsAddedProtectedMethodOnExportedPublicClass", methodAddedError);
  }

  @Test
  public void ignoresAddedProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresAddedPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPrivateMethodOnExportedPackageClass");
  }

  @Test
  public void detectsRemovedPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedPublicClass", methodRemovedError);
  }

  @Test
  public void ignoresRemovedPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsRemovedProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedProtectedMethodOnExportedPublicClass", methodRemovedError);
  }

  @Test
  public void ignoresRemovedProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresRemovedPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedProtectedMethodOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedNoExtendPublicClass");
  }

  @Test
  public void ignoresRemovedProtectedMethodOnExportedPublicClassExtendingNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedPublicClassExtendingNoExtendClass");
  }

  @Test
  public void detectsRemovedPublicMethodOnExportedNoExtendPublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedNoExtendPublicClass", methodRemovedError);
  }

  @Test
  public void detectsRemovedPublicMethodOnExportedNoInstantiatePublicClass() throws Exception {
    String[] methodRemovedError = getMethodRemovedError();
    doBrokenApiTest("detectsRemovedPublicMethodOnExportedNoInstantiatePublicClass", methodRemovedError);
  }

  @Test
  public void ignoresRemovedProtectedMethodOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedMethodOnExportedNoInstantiatePublicClass");
  }
}
