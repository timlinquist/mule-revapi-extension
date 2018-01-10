/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.DO_STUFF_METHOD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getReturnTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class MethodReturnTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodReturnTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodReturnType");
  }

  @Test
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicClass", returnTypeChangedError);
  }

  @Test
  public void ignoresChangedReturnTypeInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsChangedReturnTypeInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInProtectedMethodOnExportedPublicClass", returnTypeChangedError);
  }

  @Test
  public void ignoresChangedReturnTypeInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresChangedReturnTypeInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresChangedReturnTypeInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresChangedReturnTypeInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresChangedReturnTypeInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresChangedReturnTypeInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInPrivateMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoExtendClass");
  }

  @Test
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicNoExtendClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicNoExtendClass", returnTypeChangedError);
  }

  @Test
  public void detectsChangedReturnTypeInPublicMethodOnExportedPublicNoInstantiateClass() throws Exception {
    String[] returnTypeChangedError = getReturnTypeChangedError(ORG_FOO_A, DO_STUFF_METHOD);

    doBrokenApiTest("detectsChangedReturnTypeInPublicMethodOnExportedPublicNoInstantiateClass", returnTypeChangedError);
  }

  @Test
  public void ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoInstantiateClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangedReturnTypeInProtectedMethodOnExportedPublicNoInstantiateClass");
  }
}
