/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNoLongerStaticError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNowStaticError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class MethodStaticModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public MethodStaticModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodStaticModifier");
  }

  @Test
  public void detectsAddedStaticModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowStaticError = getMethodNowStaticError(PUBLIC, PUBLIC_STATIC);
    doBrokenApiTest("detectsAddedStaticModifierInPublicMethodOnExportedPublicClass", methodNowStaticError);
  }

  @Test
  public void ignoresAddedStaticModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsAddedStaticModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowStaticError = getMethodNowStaticError(PROTECTED, PROTECTED_STATIC);
    doBrokenApiTest("detectsAddedStaticModifierInProtectedMethodOnExportedPublicClass", methodNowStaticError);
  }

  @Test
  public void ignoresAddedStaticModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresAddedStaticModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedStaticModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedStaticModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedStaticModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedStaticModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedStaticModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInPrivateMethodOnExportedPackageClass");
  }

  @Test
  public void detectsRemovedStaticModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNoLongerStaticError = getMethodNoLongerStaticError(PUBLIC_STATIC, PUBLIC);
    doBrokenApiTest("detectsRemovedStaticModifierInPublicMethodOnExportedPublicClass", methodNoLongerStaticError);
  }

  @Test
  public void ignoresRemovedStaticModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsRemovedStaticModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNoLongerStaticError = getMethodNoLongerStaticError(PROTECTED_STATIC, PROTECTED);
    doBrokenApiTest("detectsRemovedStaticModifierInProtectedMethodOnExportedPublicClass", methodNoLongerStaticError);
  }

  @Test
  public void ignoresRemovedStaticModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInProtectedMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInPrivateMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPrivateMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPackageMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedStaticModifierInPrivateMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedStaticModifierInPrivateMethodOnExportedPackageClass");
  }
}
