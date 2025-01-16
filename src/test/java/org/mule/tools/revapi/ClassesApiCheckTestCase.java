/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static org.mule.tools.revapi.ApiErrorLogUtils.EMPTY_PARAMS;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_BAR_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_A_C;
import static org.mule.tools.revapi.ApiErrorLogUtils.ORG_FOO_B;
import static org.mule.tools.revapi.ApiErrorLogUtils.PROTECTED;
import static org.mule.tools.revapi.ApiErrorLogUtils.PUBLIC;
import static org.mule.tools.revapi.ApiErrorLogUtils.STRING;
import static org.mule.tools.revapi.ApiErrorLogUtils.getAddedClassErrorLog;
import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorNumberOfParametersChangedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getConstructorVisibilityIncreasedError;
import static org.mule.tools.revapi.ApiErrorLogUtils.getRemovedClassErrorLog;

import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.junit.MavenPluginTest;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AbstractApiCheckTestCase.ApiCheckTestCaseContextProvider.class)
public class ClassesApiCheckTestCase extends AbstractApiCheckTestCase {

  public ClassesApiCheckTestCase(MavenRuntime.MavenRuntimeBuilder builder, boolean isPromotedApi) throws Exception {
    super(builder, "class", isPromotedApi);
  }

  @MavenPluginTest
  public void detectsApiChangesInExportedSuperClassInPublicClass() throws Exception {
    String[] aConstructorNumberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A, EMPTY_PARAMS, STRING);
    String[] bConstructorNumberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_BAR_B, EMPTY_PARAMS, STRING);

    doBrokenApiTest("detectsApiChangesInExportedSuperClassInPublicClass", aConstructorNumberOfParametersChangedError,
                    bConstructorNumberOfParametersChangedError);
  }

  @MavenPluginTest
  public void detectsApiChangesInExportedSuperClassInProtectedInnerClass() throws Exception {
    String[] bConstructorNumberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_BAR_B, EMPTY_PARAMS, STRING);
    String[] cConstructorNumberOfParametersChangedError =
        getConstructorNumberOfParametersChangedError(ORG_FOO_A_C, EMPTY_PARAMS, STRING);
    String[] cConstructorVisibilityIncreasedError =
        getConstructorVisibilityIncreasedError(ORG_FOO_A_C, PROTECTED, PUBLIC, EMPTY_PARAMS, STRING);
    doBrokenApiTest("detectsApiChangesInExportedSuperClassInProtectedInnerClass", bConstructorNumberOfParametersChangedError,
                    cConstructorNumberOfParametersChangedError, cConstructorVisibilityIncreasedError);
  }

  @MavenPluginTest
  public void ignoresChangesInSuperClassInPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangesInSuperClassInPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangesInSuperClassInPrivateInnerClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangesInSuperClassInPrivateInnerClass");
  }

  @MavenPluginTest
  public void detectsAddedExportedPublicClass() throws Exception {
    String[] addedClassErrorLog = getAddedClassErrorLog();
    doBrokenApiTest("detectsAddedExportedPublicClass", addedClassErrorLog);
  }

  @MavenPluginTest
  public void ignoresAddedInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresAddedExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresAddedExportedPackageClass");
  }

  @MavenPluginTest
  public void detectsRemovedExportedPublicClass() throws Exception {
    String[] removedClassErrorLog = getRemovedClassErrorLog(ORG_FOO_B);

    doBrokenApiTest("detectsRemovedExportedPublicClass", removedClassErrorLog);
  }

  @MavenPluginTest
  public void ignoresRemovedInternalPublicClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedInternalPublicClass");
  }

  @MavenPluginTest
  public void ignoresRemovedExportedPackageClass() throws Exception {
    doUnmodifiedApiTest("ignoresRemovedExportedPackageClass");
  }

  @MavenPluginTest
  public void ignoresChangesInInternalSubClassOfExportedSuperClass() throws Exception {
    doUnmodifiedApiTest("ignoresChangesInInternalSubClassOfExportedSuperClass");
  }
}
