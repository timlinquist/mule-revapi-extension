/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A_C;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodAddedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getMethodAddedToInterfaceError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class InterfaceApiCheckTestCase extends AbstractApiCheckTestCase {

  public InterfaceApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "interface");
  }

  @Test
  public void detectsApiChangesInExportedInterfaceInProtectedInnerClass() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A_C);
    doBrokenApiTest("detectsApiChangesInExportedInterfaceInProtectedInnerClass", methodAddedToInterfaceError, methodAddedError);
  }

  @Test
  public void ignoresApiChangesInInternalInterfaceInProtectedInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInternalInterfaceInProtectedInnerClass");
  }

  @Test
  public void detectsApiChangesInExportedInterfaceInPublicClass() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    String[] methodAddedError = getMethodAddedError(ORG_FOO_A);
    doBrokenApiTest("detectsApiChangesInExportedInterfaceInPublicClass", methodAddedToInterfaceError, methodAddedError);
  }

  @Test
  public void ignoresApiChangesInInternalInterfaceInPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInternalInterfaceInPublicClass");
  }

  @Test
  public void detectsApiChangesInSuperInterfaceInExportedPublicInterface() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    doBrokenApiTest("detectsApiChangesInSuperInterfaceInExportedPublicInterface", methodAddedToInterfaceError);
  }

  @Test
  public void ignoresApiChangesInSuperInterfaceInInternalPublicInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInInternalPublicInterface");
  }

  @Test
  public void ignoresChangesInSuperInterfaceInExportedPackageInterface() throws Exception {
    doUnmodifiedApiTest("ignoresChangesInSuperInterfaceInExportedPackageInterface");
  }

  @Test
  public void ignoresApiChangesInInterfaceInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInterfaceInExportedPackageClass");
  }

  @Test
  public void ignoresApiChangesInSuperInterfaceInExportedPrivateInnerInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInExportedPrivateInnerInterface");
  }

  @Test
  public void detectsApiChangesInSuperInterfaceInExportedProtectedInnerInterface() throws Exception {
    String[] methodAddedToInterfaceError = getMethodAddedToInterfaceError();
    doBrokenApiTest("detectsApiChangesInSuperInterfaceInExportedProtectedInnerInterface", methodAddedToInterfaceError);
  }

  @Test
  public void ignoresApiChangesInSuperInterfaceInInternalProtectedInnerInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInSuperInterfaceInInternalProtectedInnerInterface");
  }

  @Test
  public void ignoresApiChangesInInterfaceInExportedPrivateInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInInterfaceInExportedPrivateInnerClass");
  }

  @Test
  public void ignoresApiChangesInExportedNoImplementInterfaceInPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInExportedNoImplementInterfaceInPublicClass");
  }

  @Test
  public void ignoresApiChangesInExportedInterfaceExtendingNoImplementInterface() throws Exception {
    doUnmodifiedApiTest("ignoresApiChangesInExportedInterfaceExtendingNoImplementInterface");
  }
}

