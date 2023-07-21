/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.getClassNoLongerExtendsFromError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getNonFinalClassInheritsFromNewClassError;

import io.takari.maven.testing.executor.MavenRuntime;
import org.junit.Test;

public class SuperClassApiCheckTestCase extends AbstractApiCheckTestCase {

  public SuperClassApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder) throws Exception {
    super(builder, "superclass");
  }

  @Test
  public void detectsAddedSuperClassInExportedPublicClass() throws Exception {
    String[] nonFinalClassInheritsFromNewClassError = getNonFinalClassInheritsFromNewClassError(ORG_FOO_A, ORG_FOO_B);

    doBrokenApiTest("detectsAddedSuperClassInExportedPublicClass", nonFinalClassInheritsFromNewClassError);
  }

  @Test
  public void ignoresAddedSuperClassInInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInInternalPublicClass");
  }

  @Test
  public void ignoresAddedSuperClassInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInExportedPackageClass");
  }

  @Test
  public void ignoresAddedSuperClassInExportedPublicFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInExportedPublicFinalClass");
  }

  @Test
  public void detectsRemovedSuperClassInExportedPublicClass() throws Exception {
    String[] classNoLongerExtendsFromError = getClassNoLongerExtendsFromError(ORG_FOO_A, ORG_FOO_B);

    doBrokenApiTest("detectsRemovedSuperClassInExportedPublicClass", classNoLongerExtendsFromError);
  }

  @Test
  public void ignoresRemovedSuperClassInInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedSuperClassInInternalPublicClass");
  }

  @Test
  public void ignoresRemovedSuperClassInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedSuperClassInExportedPackageClass");
  }
}
