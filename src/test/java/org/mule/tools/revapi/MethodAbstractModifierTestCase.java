/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_ABSTRACT;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_ABSTRACT;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNowAbstractError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class MethodAbstractModifierTestCase extends AbstractApiCheckTestCase {

  public MethodAbstractModifierTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "methodAbstractModifier");
  }

  @Test
  public void detectsAddedAbstractModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowAbstractError = getMethodNowAbstractError(PUBLIC, PUBLIC_ABSTRACT);
    doBrokenApiTest("detectsAddedAbstractModifierInPublicMethodOnExportedPublicClass", methodNowAbstractError);
  }

  @Test
  public void ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void detectsAddedAbstractModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowAbstractError = getMethodNowAbstractError(PROTECTED, PROTECTED_ABSTRACT);
    doBrokenApiTest("detectsAddedAbstractModifierInProtectedMethodOnExportedPublicClass", methodNowAbstractError);
  }

  @Test
  public void ignoresAddedAbstractModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass");
  }

  @Test
  public void ignoresAddedAbstractModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPackageMethodOnExportedPublicClass");
  }

  @Test
  public void ignoresAddedAbstractModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedAbstractModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInProtectedMethodOnExportedPackageClass");
  }

  @Test
  public void ignoresAddedAbstractModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPackageMethodOnExportedPackageClass");
  }
}
