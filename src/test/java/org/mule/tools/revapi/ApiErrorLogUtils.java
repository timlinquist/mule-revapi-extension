/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import static java.lang.String.format;

/**
 * Provides utilities to create error messages lines that can be used to match against the real log generated during the Maven
 * Revapi plugin.
 * <p/>
 * NOTE: errors messages have different values that can be parameterized, but this utilities parametrize only the values that
 * need to be variable according to the current test suite.
 */
public class ApiErrorLogUtils {

  private static final String ABSTRACT = "abstract";
  private static final String STATIC = "static";
  private static final String FINAL = "final";

  static final String JAVA_PREFIX = "java.";
  static final String OBJECT = JAVA_PREFIX + "lang.Object";
  static final String STRING = JAVA_PREFIX + "lang.String";
  static final String EMPTY_PARAMS = "";
  static final String ORG_BAR_B = "org.bar.B";
  static final String ORG_FOO_A_B = "org.foo.A.B";
  static final String ORG_FOO_A_C = "org.foo.A.C";
  static final String ORG_FOO_A = "org.foo.A";
  static final String ORG_FOO_B = "org.foo.B";
  static final String DO_STUFF_METHOD = "doStuff";
  static final String B_FIELD = "b";
  static final String API_ERROR_JUSTIFICATION =
      "[ERROR] \"justification\": <<<<< ADD YOUR EXPLANATION FOR THE NECESSITY OF THIS CHANGE >>>>>";

  static final String PACKAGE = "package";
  static final String PRIVATE = "private";
  static final String PROTECTED = "protected";
  static final String PROTECTED_STATIC = PROTECTED + " " + STATIC;
  static final String PROTECTED_FINAL = PROTECTED + " " + FINAL;
  static final String PROTECTED_STATIC_FINAL = PROTECTED + " " + STATIC + " " + FINAL;
  static final String PROTECTED_ABSTRACT = PROTECTED + " " + ABSTRACT;
  static final String PUBLIC = "public";
  static final String PUBLIC_STATIC = PUBLIC + " " + STATIC;
  static final String PUBLIC_FINAL = PUBLIC + " " + FINAL;
  static final String PUBLIC_STATIC_FINAL = PUBLIC + " " + STATIC + " " + FINAL;
  static final String PUBLIC_ABSTRACT = PUBLIC + " " + ABSTRACT;

  private static final String CLASS = "class";
  private static final String PARAMETER = "parameter";
  private static final String METHOD = "method";
  private static final String FIELD = "field";
  private static final String CONSTRUCTOR = "constructor";
  private static final String VOID = "void";
  private static final String CODE_SEPARATOR = ".";
  private static final String CLASS_PREFIX = JAVA_PREFIX + CLASS + CODE_SEPARATOR;
  private static final String CLASS_REMOVED = CLASS_PREFIX + "removed";
  private static final String CLASS_ADDED = CLASS_PREFIX + "added";
  private static final String CLASS_NOW_FINAL = CLASS_PREFIX + "nowFinal";
  private static final String CLASS_VISIBILITY_REDUCED = CLASS_PREFIX + "visibilityReduced";
  private static final String CLASS_NO_LONGER_INHERITS_FROM_CLASS = CLASS_PREFIX + "noLongerInheritsFromClass";
  private static final String CLASS_NON_FINAL_CLASS_INHERITS_FROM_NEW_CLASS =
      CLASS_PREFIX + "nonFinalClassInheritsFromNewClass";
  private static final String METHOD_PREFIX = JAVA_PREFIX + METHOD + CODE_SEPARATOR;
  private static final String METHOD_PARAMETER_TYPE_CHANGED = METHOD_PREFIX + "parameterTypeChanged";
  private static final String METHOD_RETURN_TYPE_CHANGED = METHOD_PREFIX + "returnTypeChanged";
  private static final String FIELD_PREFIX = JAVA_PREFIX + FIELD + CODE_SEPARATOR;
  private static final String FIELD_TYPE_CHANGED = FIELD_PREFIX + "typeChanged";
  private static final String METHOD_VISIBILITY_REDUCED = METHOD_PREFIX + "visibilityReduced";
  private static final String METHOD_NOW_STATIC = METHOD_PREFIX + "nowStatic";
  private static final String METHOD_NO_LONGER_STATIC = METHOD_PREFIX + "noLongerStatic";
  private static final String METHOD_NUMBER_OF_PARAMETERS_CHANGED = METHOD_PREFIX + "numberOfParametersChanged";
  private static final String METHOD_NOW_FINAL = METHOD_PREFIX + "nowFinal";
  private static final String METHOD_ADDED = METHOD_PREFIX + "added";
  private static final String METHOD_REMOVED = METHOD_PREFIX + "removed";
  private static final String METHOD_NOW_ABSTRACT = METHOD_PREFIX + "nowAbstract";
  private static final String METHOD_ADDED_TO_INTERFACE = METHOD_PREFIX + "addedToInterface";
  private static final String FIELD_VISIBILITY_REDUCED = FIELD_PREFIX + "visibilityReduced";
  private static final String FIELD_NOW_FINAL = FIELD_PREFIX + "nowFinal";
  private static final String FIELD_REMOVED = FIELD_PREFIX + "removed";
  private static final String PARAM_MARKER = "===";

  private ApiErrorLogUtils() {}

  /**
   * Generates error log for {@value CLASS_REMOVED }
   *
   * @param className name of the removed class
   * @return the expected error log lines for this error code
   */
  public static String[] getRemovedClassErrorLog(String className) {
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_REMOVED),
        getOldElementLine(CLASS, className),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value CLASS_ADDED }
   *
   * @return the expected error log lines for this error code
   */
  public static String[] getAddedClassErrorLog() {
    String className = ORG_FOO_B;
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_ADDED),
        getNewElementLine(CLASS, className),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value CLASS_NOW_FINAL }
   *
   * @param className name of the modified class
   * @param oldModifiers class modifiers declared in the old version of the class
   * @param newModifiers class modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getClassNowFinalErrorLog(String className, String oldModifiers, String newModifiers) {
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_NOW_FINAL),
        getOldElementLine(CLASS, className),
        getNewElementLine(CLASS, className),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value CLASS_VISIBILITY_REDUCED }
   *
   * @param className name of the modified class
   * @param oldVisibility class visibility declared in the old version of the class
   * @param newVisibility class visibility declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getClassVisibilityReducedError(String className, String oldVisibility, String newVisibility) {
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_VISIBILITY_REDUCED),
        getOldElementLine(CLASS, className),
        getNewElementLine(CLASS, className),
        getOldVisibilityLine(oldVisibility),
        getNewVisibilityLine(newVisibility),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION};

    return errorLog;
  }

  /**
   * Generates error log for {@value CLASS_NO_LONGER_INHERITS_FROM_CLASS}
   *
   * @param className name of the modified class
   * @param superClassName name of the superclass declared in the old version of {@code className}
   * @return the expected error log lines for this error code
   */
  public static String[] getClassNoLongerExtendsFromError(String className, String superClassName) {
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_NO_LONGER_INHERITS_FROM_CLASS),
        getOldElementLine(CLASS, className),
        getNewElementLine(CLASS, className),
        getSuperClassLine(superClassName),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value CLASS_NON_FINAL_CLASS_INHERITS_FROM_NEW_CLASS }
   *
   * @param className name of the modified class
   * @param superClassName name of the superclass declared in the new version of {@code className}
   * @return the expected error log lines for this error code
   */
  public static String[] getNonFinalClassInheritsFromNewClassError(String className, String superClassName) {
    String[] errorLog = new String[] {
        getErrorCodeLine(CLASS_NON_FINAL_CLASS_INHERITS_FROM_NEW_CLASS),
        getOldElementLine(CLASS, className),
        getNewElementLine(CLASS, className),
        getSuperClassLine(superClassName),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getElementKindLine(CLASS),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_PARAMETER_TYPE_CHANGED }
   *
   * @param className name of the modified class
   * @param methodName name of the method that changed in the new version of {@code className}
   * @return the expected error log lines for this error code
   */
  public static String[] getParameterTypeChangedError(String className, String methodName) {
    String oldType = OBJECT;
    String newType = STRING;
    String oldParams = PARAM_MARKER + oldType + PARAM_MARKER;
    String newParams = PARAM_MARKER + newType + PARAM_MARKER;

    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_PARAMETER_TYPE_CHANGED),
        getOldElementLine(PARAMETER, getMethod(className, methodName, VOID, oldParams)),
        getNewElementLine(PARAMETER, getMethod(className, methodName, VOID, newParams)),
        getOldTypeLine(oldType),
        getNewTypeLine(newType),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getParameterIndexLine(0),
        getElementKindLine(PARAMETER),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_RETURN_TYPE_CHANGED }
   *
   * @param className name of the modified class
   * @param methodName name of the method that changed in the new version of {@code className}
   * @return the expected error log lines for this error code
   */
  public static String[] getReturnTypeChangedError(String className, String methodName) {
    String oldType = VOID;
    String newType = STRING;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_RETURN_TYPE_CHANGED),
        getOldElementLine(METHOD, getMethod(className, methodName, oldType, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, newType, EMPTY_PARAMS)),
        getOldTypeLine(oldType),
        getNewTypeLine(newType),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };
    return errorLog;
  }

  /**
   * Generates error log for {@value FIELD_TYPE_CHANGED }
   *
   * @param className name of the modified class
   * @param fieldName name of the field that changed in the new version of {@code className}
   * @return the expected error log lines for this error code
   */
  public static String[] getFieldTypeChangedError(String className, String fieldName) {
    String[] errorLog = new String[] {
        getErrorCodeLine(FIELD_TYPE_CHANGED),
        getOldElementLine(FIELD, getFieldName(className, fieldName)),
        getNewElementLine(FIELD, getFieldName(className, fieldName)),
        getOldTypeLine(ORG_BAR_B),
        getNewTypeLine(STRING),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getFieldNameLine(fieldName),
        getElementKindLine(FIELD),
        API_ERROR_JUSTIFICATION
    };
    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_VISIBILITY_REDUCED }
   *
   * @param oldVisibility method visibility declared in the old version of the class
   * @param newVisibility method visibility declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodVisibilityReducedError(String oldVisibility, String newVisibility) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;

    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_VISIBILITY_REDUCED),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getOldVisibilityLine(oldVisibility),
        getNewVisibilityLine(newVisibility),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION};

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_NOW_STATIC }
   *
   * @param oldModifiers method modifiers declared in the old version of the class
   * @param newModifiers method modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodNowStaticError(String oldModifiers, String newModifiers) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;

    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_NOW_STATIC),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_NO_LONGER_STATIC }
   *
   * @param oldModifiers method modifiers declared in the old version of the class
   * @param newModifiers method modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodNoLongerStaticError(String oldModifiers, String newModifiers) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_NO_LONGER_STATIC),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };
    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_NUMBER_OF_PARAMETERS_CHANGED} for standard methods.
   *
   * @param oldParams parameters declared in the old version of the class
   * @param newParams parameters declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getNumberOfParametersChangedError(String oldParams, String newParams) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_NUMBER_OF_PARAMETERS_CHANGED),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, oldParams)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, newParams)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_NUMBER_OF_PARAMETERS_CHANGED} for constructors
   *
   * @param oldParams parameters declared in the old version of the class
   * @param newParams parameters declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getConstructorNumberOfParametersChangedError(String className, String oldParams, String newParams) {
    String methodName = "<init>";
    String[] errorLog = new String[] {
        getErrorCodeLine(ApiErrorLogUtils.METHOD_NUMBER_OF_PARAMETERS_CHANGED),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, oldParams)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, newParams)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(CONSTRUCTOR),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_RETURN_TYPE_CHANGED }
   *
   * @param oldModifiers method modifiers declared in the old version of the class
   * @param newModifiers method modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodNowFinalError(String oldModifiers, String newModifiers) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_NOW_FINAL),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_ADDED }
   *
   * @param className name of the modified class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodAddedError(String className) {
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_ADDED),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_REMOVED }
   *
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodRemovedError() {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_REMOVED),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_NOW_ABSTRACT }
   *
   * @param oldModifiers method modifiers declared in the old version of the class
   * @param newModifiers method modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodNowAbstractError(String oldModifiers, String newModifiers) {
    String className = ORG_FOO_A;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_NOW_ABSTRACT),
        getOldElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value METHOD_ADDED_TO_INTERFACE }
   *
   * @return the expected error log lines for this error code
   */
  public static String[] getMethodAddedToInterfaceError() {
    String className = ORG_BAR_B;
    String methodName = DO_STUFF_METHOD;
    String[] errorLog = new String[] {
        getErrorCodeLine(METHOD_ADDED_TO_INTERFACE),
        getNewElementLine(METHOD, getMethod(className, methodName, VOID, EMPTY_PARAMS)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getMethodNameLine(methodName),
        getElementKindLine(METHOD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value FIELD_VISIBILITY_REDUCED }
   *
   * @param oldVisibility class visibility declared in the old version of the class
   * @param newVisibility class visibility declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getFieldVisibilityReducedError(String oldVisibility, String newVisibility) {
    String className = ORG_FOO_A;
    String fieldName = FIELD;
    String[] errorLog = new String[] {
        getErrorCodeLine(FIELD_VISIBILITY_REDUCED),
        getOldElementLine(FIELD, getFieldName(className, fieldName)),
        getNewElementLine(FIELD, getFieldName(className, fieldName)),
        getOldVisibilityLine(oldVisibility),
        getNewVisibilityLine(newVisibility),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getFieldNameLine(fieldName),
        getElementKindLine(FIELD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value FIELD_NOW_FINAL }
   *
   * @param oldModifiers class modifiers declared in the old version of the class
   * @param newModifiers class modifiers declared in the new version of the class
   * @return the expected error log lines for this error code
   */
  public static String[] getFieldNowFinalError(String oldModifiers, String newModifiers) {
    String className = ORG_FOO_A;
    String fieldName = B_FIELD;
    String[] errorLog = new String[] {
        getErrorCodeLine(FIELD_NOW_FINAL),
        getOldElementLine(FIELD, getFieldName(className, fieldName)),
        getNewElementLine(FIELD, getFieldName(className, fieldName)),
        getOldModifiersLine(oldModifiers),
        getNewModifiersLine(newModifiers),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getFieldNameLine(fieldName),
        getElementKindLine(FIELD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  /**
   * Generates error log for {@value FIELD_REMOVED }
   *
   * @return the expected error log lines for this error code
   */
  public static String[] getFieldRemovedError() {
    String className = ORG_FOO_A;
    String fieldName = B_FIELD;
    String[] errorLog = new String[] {
        getErrorCodeLine(FIELD_REMOVED),
        getOldElementLine(FIELD, getFieldName(className, fieldName)),
        getPackageLine(className),
        getClassSimpleNameLine(className),
        getFieldNameLine(fieldName),
        getElementKindLine(FIELD),
        API_ERROR_JUSTIFICATION
    };

    return errorLog;
  }

  private static String getSimpleName(String name) {
    int index = name.lastIndexOf(CODE_SEPARATOR);
    return name.substring(index + 1);
  }

  private static String getPackageName(String name) {
    int index = name.lastIndexOf(CODE_SEPARATOR);
    String packageName = name.substring(0, index);

    if (packageName.toLowerCase().equals(packageName)) {
      return packageName;
    }

    index = packageName.lastIndexOf(CODE_SEPARATOR);
    return packageName.substring(0, index);
  }

  private static String getMethod(String className, String methodName, String returnType, String parameters) {
    return returnType + " " + className + "::" + methodName + "(" + parameters + ")";
  }

  private static String getFieldName(String className, String fieldName) {
    return className + CODE_SEPARATOR + fieldName;
  }

  private static String getClassSimpleNameLine(String className) {
    return "[ERROR] \"classSimpleName\": \"" + getSimpleName(className) + "\",";
  }

  private static String getPackageLine(String className) {
    return "[ERROR] \"package\": \"" + getPackageName(className) + "\",";
  }

  private static String getErrorCodeLine(String errorCode) {
    return format("[ERROR] \"code\": \"%s\",", errorCode);
  }

  private static String getOldModifiersLine(String oldModifiers) {
    return format("[ERROR] \"oldModifiers\": \"%s\",", oldModifiers);
  }

  private static String getNewModifiersLine(String newModifiers) {
    return format("[ERROR] \"newModifiers\": \"%s\",", newModifiers);
  }

  private static String getNewVisibilityLine(String newVisibility) {
    return format("[ERROR] \"newVisibility\": \"%s\",", newVisibility);
  }

  private static String getOldVisibilityLine(String oldVisibility) {
    return format("[ERROR] \"oldVisibility\": \"%s\",", oldVisibility);
  }

  private static String getSuperClassLine(String superClassName) {
    return format("[ERROR] \"superClass\": \"%s\",", superClassName);
  }

  private static String getOldElementLine(String type, String element) {
    return format("[ERROR] \"old\": \"%s %s\",", type, element);
  }

  private static String getNewElementLine(String type, String element) {
    return format("[ERROR] \"new\": \"%s %s\",", type, element);
  }

  private static String getFieldNameLine(String fieldName) {
    return format("[ERROR] \"fieldName\": \"%s\",", fieldName);
  }

  private static String getMethodNameLine(String methodName) {
    return format("[ERROR] \"methodName\": \"%s\",", methodName);
  }

  private static String getOldTypeLine(String type) {
    return format("[ERROR] \"oldType\": \"%s\",", type);
  }

  private static String getNewTypeLine(String type) {
    return format("[ERROR] \"newType\": \"%s\",", type);
  }

  private static String getElementKindLine(String elementKind) {
    return format("[ERROR] \"elementKind\": \"%s\",", elementKind);
  }

  private static String getParameterIndexLine(int index) {
    return format("[ERROR] \"parameterIndex\": \"%s\",", index);
  }
}
