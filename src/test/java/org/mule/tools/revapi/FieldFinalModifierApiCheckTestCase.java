/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldNowFinalError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class FieldFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "fieldFinalModifier", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedFinalModifierInExportedPublicField() throws Exception {
    String[] fieldNowFinalError = getFieldNowFinalError(PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInExportedPublicField", fieldNowFinalError);
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInInternalPublicField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInInternalPublicField");
  }

  @MavenPluginTest
  public void detectsAddedFinalModifierInExportedProtectedField() throws Exception {
    String[] fieldNowFinalError = getFieldNowFinalError(PROTECTED, PROTECTED_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInExportedProtectedField", fieldNowFinalError);
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInInternalProtectedField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInInternalProtectedField");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInExportedPackageField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedPackageField");
  }

  @MavenPluginTest
  public void ignoresAddedFinalModifierInExportedPrivateField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedPrivateField");
  }

  @MavenPluginTest
  @Disabled("Revapi considers this a API change, but the class is final, so no subclasses will access this protected field")
  public void ignoresAddedFinalModifierInExportedProtectedFieldOnFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedProtectedFieldOnFinalClass");
  }
}
