/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
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
import org.junit.Test;

public class ClassFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "classFinalModifier");
  }

  @Test
  public void detectsAddedStaticModifierInExportedPublicClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A, PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedPublicClass", classNowFinalErrorLog);
  }

  @Test
  public void ignoresPublicToPackageInternalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalPublicClass");
  }

  @Test
  public void ignoresAddedStaticModifierInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPackageClass");
  }

  @Test
  public void detectsAddedStaticModifierInExportedPublicInnerClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A_B, PUBLIC_STATIC, PUBLIC_STATIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedPublicInnerClass", classNowFinalErrorLog);
  }

  @Test
  public void ignoresAddedStaticModifierInInternalPublicInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalPublicInnerClass");
  }

  @Test
  public void detectsAddedStaticModifierInExportedProtectedInnerClass() throws Exception {
    String[] classNowFinalErrorLog = getClassNowFinalErrorLog(ORG_FOO_A_B, PROTECTED_STATIC, PROTECTED_STATIC_FINAL);
    doBrokenApiTest("detectsAddedStaticModifierInExportedProtectedInnerClass", classNowFinalErrorLog);
  }

  @Test
  public void ignoresAddedStaticModifierInInternalProtectedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInInternalProtectedInnerClass");
  }

  @Test
  public void ignoresAddedStaticModifierInExportedPackageInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPackageInnerClass");
  }

  @Test
  public void ignoresAddedStaticModifierInExportedPrivateInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedStaticModifierInExportedPrivateInnerClass");
  }

}
