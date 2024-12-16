/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_ABSTRACT;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_ABSTRACT;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodNowAbstractError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class MethodAbstractModifierTestCase extends AbstractApiCheckTestCase {

  public MethodAbstractModifierTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "methodAbstractModifier", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedAbstractModifierInPublicMethodOnExportedPublicClass() throws Exception {
    String[] methodNowAbstractError = getMethodNowAbstractError(PUBLIC, PUBLIC_ABSTRACT);
    doBrokenApiTest("detectsAddedAbstractModifierInPublicMethodOnExportedPublicClass", methodNowAbstractError);
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsAddedAbstractModifierInProtectedMethodOnExportedPublicClass() throws Exception {
    String[] methodNowAbstractError = getMethodNowAbstractError(PROTECTED, PROTECTED_ABSTRACT);
    doBrokenApiTest("detectsAddedAbstractModifierInProtectedMethodOnExportedPublicClass", methodNowAbstractError);
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInProtectedMethodOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInPackageMethodOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPackageMethodOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInPublicMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPublicMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInProtectedMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInProtectedMethodOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedAbstractModifierInPackageMethodOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedAbstractModifierInPackageMethodOnExportedPackageClass");
  }
}
