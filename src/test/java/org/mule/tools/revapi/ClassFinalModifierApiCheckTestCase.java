/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_STATIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_STATIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_STATIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.getClassNowFinalErrorLog;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class ClassFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "classFinalModifier", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedStaticModifierInExportedPublicClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A, PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedPublicClass", classNowFinalErrorLog);
  }

  @MavenPluginTest
  public void ignoresPublicToPackageInternalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPackageClass");
  }

  @MavenPluginTest
  public void detectsAddedStaticModifierInExportedPublicInnerClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A_B, PUBLIC_STATIC, PUBLIC_STATIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedPublicInnerClass", classNowFinalErrorLog);
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInInternalPublicInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalPublicInnerClass");
  }

  @MavenPluginTest
  public void detectsAddedStaticModifierInExportedProtectedInnerClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A_B, PROTECTED_STATIC, PROTECTED_STATIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedProtectedInnerClass", classNowFinalErrorLog);
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInInternalProtectedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalProtectedInnerClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInExportedPackageInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPackageInnerClass");
  }

  @MavenPluginTest
  public void ignoresAddedStaticModifierInExportedPrivateInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPrivateInnerClass");
  }

}
