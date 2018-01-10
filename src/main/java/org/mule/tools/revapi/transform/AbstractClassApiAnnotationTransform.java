/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi.transform;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.revapi.java.spi.Code.FIELD_REMOVED;
import static org.revapi.java.spi.Code.FIELD_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_ADDED;
import static org.revapi.java.spi.Code.METHOD_NUMBER_OF_PARAMETERS_CHANGED;
import static org.revapi.java.spi.Code.METHOD_PARAMETER_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_REMOVED;
import static org.revapi.java.spi.Code.METHOD_RETURN_TYPE_CHANGED;

import org.revapi.Element;
import org.revapi.java.model.FieldElement;
import org.revapi.java.model.MethodElement;

/**
 * Base class for implementing annotation based difference transformations on Class and Enum types.
 *
 * @since 1.1
 */
public abstract class AbstractClassApiAnnotationTransform extends AbstractApiAnnotationTransform {

  AbstractClassApiAnnotationTransform(String id) {
    super(id);
  }

  @Override
  protected String[] getDifferenceCodes() {
    return new String[] {
        FIELD_REMOVED.code(),
        FIELD_TYPE_CHANGED.code(),
        METHOD_REMOVED.code(),
        METHOD_ADDED.code(),
        METHOD_NUMBER_OF_PARAMETERS_CHANGED.code(),
        METHOD_PARAMETER_TYPE_CHANGED.code(),
        METHOD_RETURN_TYPE_CHANGED.code()
    };
  }

  boolean isPublicConstructor(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getKind().equals(CONSTRUCTOR)
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PUBLIC);
  }

  boolean isProtectedMethod(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }

  boolean isProtectedConstructor(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getKind().equals(CONSTRUCTOR)
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }

  boolean isProtectedField(Element element) {
    return element instanceof FieldElement
        && ((FieldElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }
}
