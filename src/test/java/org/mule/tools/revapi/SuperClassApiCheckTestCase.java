/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.getClassNoLongerExtendsFromError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getNonFinalClassInheritsFromNewClassError;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class SuperClassApiCheckTestCase extends AbstractApiCheckTestCase {

  public SuperClassApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "superclass", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsAddedSuperClassInExportedPublicClass() throws Exception {
    String[] nonFinalClassInheritsFromNewClassError = getNonFinalClassInheritsFromNewClassError(ORG_FOO_A, ORG_FOO_B);

    doBrokenApiTest("detectsAddedSuperClassInExportedPublicClass", nonFinalClassInheritsFromNewClassError);
  }

  @MavenPluginTest
  public void ignoresAddedSuperClassInInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedSuperClassInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresAddedSuperClassInExportedPublicFinalClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedSuperClassInExportedPublicFinalClass");
  }

  @MavenPluginTest
  public void detectsRemovedSuperClassInExportedPublicClass() throws Exception {
    String[] classNoLongerExtendsFromError = getClassNoLongerExtendsFromError(ORG_FOO_A, ORG_FOO_B);

    doBrokenApiTest("detectsRemovedSuperClassInExportedPublicClass", classNoLongerExtendsFromError);
  }

  @MavenPluginTest
  public void ignoresRemovedSuperClassInInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedSuperClassInInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedSuperClassInExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedSuperClassInExportedPackageClass");
  }
}
