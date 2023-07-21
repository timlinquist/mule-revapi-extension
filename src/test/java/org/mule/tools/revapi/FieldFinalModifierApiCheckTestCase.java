/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC_FINAL;
import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldNowFinalError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Ignore;
import org.junit.Test;

public class FieldFinalModifierApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldFinalModifierApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "fieldFinalModifier");
  }

  @Test
  public void detectsAddedFinalModifierInExportedPublicField() throws Exception {
    String[] fieldNowFinalError = getFieldNowFinalError(PUBLIC, PUBLIC_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInExportedPublicField", fieldNowFinalError);
  }

  @Test
  public void ignoresAddedFinalModifierInInternalPublicField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInInternalPublicField");
  }

  @Test
  public void detectsAddedFinalModifierInExportedProtectedField() throws Exception {
    String[] fieldNowFinalError = getFieldNowFinalError(PROTECTED, PROTECTED_FINAL);
    doBrokenApiTest("detectsAddedFinalModifierInExportedProtectedField", fieldNowFinalError);
  }

  @Test
  public void ignoresAddedFinalModifierInInternalProtectedField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInInternalProtectedField");
  }

  @Test
  public void ignoresAddedFinalModifierInExportedPackageField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedPackageField");
  }

  @Test
  public void ignoresAddedFinalModifierInExportedPrivateField() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedPrivateField");
  }

  @Test
  @Ignore("Revapi considers this a API change, but the class is final, so no subclasses will access this protected field")
  public void ignoresAddedFinalModifierInExportedProtectedFieldOnFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedFinalModifierInExportedProtectedFieldOnFinalClass");
  }
}
