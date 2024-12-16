/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldRemovedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class FieldApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "field", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsRemovedPublicFieldOnExportedPublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedPublicFieldOnExportedPublicClass", fieldRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedPublicFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedProtectedFieldOnExportedPublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedProtectedFieldOnExportedPublicClass", fieldRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPackageFieldOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageFieldOnExportedPublicClass");
  }

  @MavenPluginTest
  @Disabled("Revapi considers this a API change, but the class is final, so no subclasses will access this protected field")
  public void ignoresRemovedProtectedFieldOnExportedPublicFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedPublicFinalClass");
  }

  @MavenPluginTest
  public void ignoresRemovedChangeInExportedPrivateInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateFieldOnExportedPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPublicFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicFieldOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPackageFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageFieldOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedPrivateFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateFieldOnExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedFieldOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedPublicFieldOnExportedNoExtendPublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedPublicFieldOnExportedNoExtendPublicClass", fieldRemovedError);
  }

  @MavenPluginTest
  public void detectsRemovedPublicFieldOnExportedNoInstantiatePublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedPublicFieldOnExportedNoInstantiatePublicClass", fieldRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedFieldOnExportedNoInstantiatedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedNoInstantiatedPublicClass");
  }
}
