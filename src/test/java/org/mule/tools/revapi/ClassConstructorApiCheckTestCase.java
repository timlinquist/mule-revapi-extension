/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorRemovedError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class ClassConstructorApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassConstructorApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "classConstructor", isPromotedApi);
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void detectsRemovedPublicConstructorOnExportedNoExtendPublicClass() throws Exception {
    String[] methodRemovedError = getConstructorRemovedError();
    doBrokenApiTest("detectsRemovedPublicConstructorOnExportedNoExtendPublicClass", methodRemovedError);
  }

  @MavenPluginTest
  public void ignoresRemovedPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedProtectedConstructorOnExportedNoExtendPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedConstructorOnExportedNoExtendPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedPublicConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedPublicConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedProtectedConstructorOnExportedNoInstantiatePublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedProtectedConstructorOnExportedNoInstantiatePublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedProtectedConstructorOnExportedNoInstantiatePublicClass");
  }
}
