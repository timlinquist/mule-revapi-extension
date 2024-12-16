/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A_C;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodAddedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodAddedToInterfaceError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class InterfaceApiCheckTestCase extends AbstractApiCheckTestCase {

  public InterfaceApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "interface", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsApiChangesInExportedInterfaceInProtectedInnerClass() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A_C);
    doBrokenApiTest("detectsApiChangesInExportedInterfaceInProtectedInnerClass", methodAddedToInterfaceError, methodAddedError);
  }

  @MavenPluginTest
  public void ignoresApiChangesInInternalInterfaceInProtectedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInternalInterfaceInProtectedInnerClass");
  }

  @MavenPluginTest
  public void detectsApiChangesInExportedInterfaceInPublicClass() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsApiChangesInExportedInterfaceInPublicClass", methodAddedToInterfaceError, methodAddedError);
  }

  @MavenPluginTest
  public void ignoresApiChangesInInternalInterfaceInPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInternalInterfaceInPublicClass");
  }

  @MavenPluginTest
  public void detectsApiChangesInSuperInterfaceInExportedPublicInterface() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    doBrokenApiTest("detectsApiChangesInSuperInterfaceInExportedPublicInterface", methodAddedToInterfaceError);
  }

  @MavenPluginTest
  public void ignoresApiChangesInSuperInterfaceInInternalPublicInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInInternalPublicInterface");
  }

  @MavenPluginTest
  public void ignoresChangesInSuperInterfaceInExportedPackageInterface() throws Exception {
    doUnmodifiedApiTest("ignoresChangesInSuperInterfaceInExportedPackageInterface");
  }

  @MavenPluginTest
  public void ignoresApiChangesInInterfaceInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInterfaceInExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresApiChangesInSuperInterfaceInExportedPrivateInnerInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInExportedPrivateInnerInterface");
  }

  @MavenPluginTest
  public void detectsApiChangesInSuperInterfaceInExportedProtectedInnerInterface() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    doBrokenApiTest("detectsApiChangesInSuperInterfaceInExportedProtectedInnerInterface", methodAddedToInterfaceError);
  }

  @MavenPluginTest
  public void ignoresApiChangesInSuperInterfaceInInternalProtectedInnerInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInInternalProtectedInnerInterface");
  }

  @MavenPluginTest
  public void ignoresApiChangesInInterfaceInExportedPrivateInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInterfaceInExportedPrivateInnerClass");
  }

  @MavenPluginTest
  public void ignoresApiChangesInExportedNoImplementInterfaceInPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInExportedNoImplementInterfaceInPublicClass");
  }

  @MavenPluginTest
  public void ignoresApiChangesInExportedInterfaceExtendingNoImplementInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInExportedInterfaceExtendingNoImplementInterface");
  }
}

