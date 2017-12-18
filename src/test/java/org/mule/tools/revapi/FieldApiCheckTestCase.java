/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.getFieldRemovedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Ignore;
import org.junit.Test;

public class FieldApiCheckTestCase extends AbstractApiCheckTestCase {

  public FieldApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "field");
  }

  @Test
  public void detectsRemovedPublicFieldOnExportedPublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedPublicFieldOnExportedPublicClass", fieldRemovedError);
  }

  @Test
  public void ignoresRemovedPublicFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicFieldOnInternalPublicClass");
  }

  @Test
  public void detectsRemovedProtectedFieldOnExportedPublicClass() throws Exception {
    String[] fieldRemovedError = getFieldRemovedError();
    doBrokenApiTest("detectsRemovedProtectedFieldOnExportedPublicClass", fieldRemovedError);
  }

  @Test
  public void ignoresRemovedProtectedFieldOnInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnInternalPublicClass");
  }

  @Test
  public void ignoresRemovedPackageFieldOnExportedPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageFieldOnExportedPublicClass");
  }

  @Test
  @Ignore("Revapi considers this a API change, but the class is final, so no subclasses will access this protected field")
  public void ignoresRemovedProtectedFieldOnExportedPublicFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedPublicFinalClass");
  }

  @Test
  public void ignoresRemovedChangeInExportedPrivateInstanceField() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateFieldOnExportedPublicClass");
  }

  @Test
  public void ignoresRemovedPublicFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicFieldOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedProtectedFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedFieldOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedPackageFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPackageFieldOnExportedPackageClass");
  }

  @Test
  public void ignoresRemovedPrivateFieldOnExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPrivateFieldOnExportedPackageClass");
  }
}
