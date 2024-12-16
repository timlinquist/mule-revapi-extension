/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.B_FIELD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class FieldTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "fieldType", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsTypeChangeInExportedPublicInstanceField() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedPublicInstanceField", typeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeChangeInInternalPublicInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInInternalPublicInstanceField");
  }

  @MavenPluginTest
  public void detectsTypeChangeInExportedProtectedInstanceField() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedProtectedInstanceField", typeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeChangeInInternalProtectedInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInInternalProtectedInstanceField");
  }

  @MavenPluginTest
  public void ignoresTypeChangeInExportedPackageInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedPackageInstanceField");
  }

  @MavenPluginTest
  public void ignoresTypeChangeInExportedPrivateInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedPrivateInstanceField");
  }

  @MavenPluginTest
  public void ignoresTypeChangeInExportedProtectedInstanceFieldFromNoExtendClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedProtectedInstanceFieldFromNoExtendClass");
  }

  @MavenPluginTest
  public void detectsTypeChangeInExportedPublicInstanceFieldFromNoExtendClass() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedPublicInstanceFieldFromNoExtendClass", typeChangedError);
  }

  @MavenPluginTest
  public void detectsTypeChangeInExportedPublicInstanceFieldFromNoInstantiateClass() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedPublicInstanceFieldFromNoInstantiateClass", typeChangedError);
  }

  @MavenPluginTest
  public void ignoresTypeChangeInExportedProtectedInstanceFieldFromNoInstantiateClass() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedProtectedInstanceFieldFromNoInstantiateClass");
  }

}
