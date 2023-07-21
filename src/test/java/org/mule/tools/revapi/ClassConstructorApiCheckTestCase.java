/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorRemovedError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class ClassConstructorApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassConstructorApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "classConstructor");
  }

  @Test
  public void ignoresRemovedProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @Test
  public void detectsRemovedPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] methodRemovedError = getConstructorRemovedError();
    doBrokenApiTest("detectsRemovedPublicConstructorOnExportedNoExtendPublicClass", methodRemovedError);
  }

  @Test
  public void ignoresRemovedPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresAddedProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @Test
  public void ignoresAddedPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresRemovedProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @Test
  public void ignoresAddedProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedConstructorOnExportedNoInstantiatePublicClass");
  }
}
