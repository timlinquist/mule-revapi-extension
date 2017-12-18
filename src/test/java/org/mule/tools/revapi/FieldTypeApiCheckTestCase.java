/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.B_FIELD;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldTypeChangedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class FieldTypeApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldTypeApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "fieldType");
  }

  @Test
  public void detectsTypeChangeInExportedPublicInstanceField() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedPublicInstanceField", typeChangedError);
  }

  @Test
  public void ignoresTypeChangeInInternalPublicInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInInternalPublicInstanceField");
  }

  @Test
  public void detectsTypeChangeInExportedProtectedInstanceField() throws Exception {
    String[] typeChangedError = getFieldTypeChangedError(ORG_FOO_A, B_FIELD);

    doBrokenApiTest("detectsTypeChangeInExportedProtectedInstanceField", typeChangedError);
  }

  @Test
  public void ignoresTypeChangeInInternalProtectedInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInInternalProtectedInstanceField");
  }

  @Test
  public void ignoresTypeChangeInExportedPackageInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedPackageInstanceField");
  }

  @Test
  public void ignoresTypeChangeInExportedPrivateInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresTypeChangeInExportedPrivateInstanceField");
  }
}
