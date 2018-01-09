/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi.transform;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.PROTECTED;
import static org.revapi.java.spi.Code.FIELD_REMOVED;
import static org.revapi.java.spi.Code.FIELD_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_ADDED;
import static org.revapi.java.spi.Code.METHOD_NUMBER_OF_PARAMETERS_CHANGED;
import static org.revapi.java.spi.Code.METHOD_PARAMETER_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_REMOVED;
import static org.revapi.java.spi.Code.METHOD_RETURN_TYPE_CHANGED;
import org.mule.api.annotation.NoExtend;

import java.util.HashMap;
import java.util.Map;

import org.revapi.Element;
import org.revapi.java.model.FieldElement;
import org.revapi.java.model.MethodElement;
import org.revapi.java.model.TypeElement;

/**
 * Transforms API differences ignoring the ones that correspond to valid changes on classes annotated with {@link NoExtend}
 *
 * @since 1.1
 */
public class NoExtendApiAnnotationTransform extends AbstractApiAnnotationTransform {

  /**
   * Creates a new transformer
   */
  public NoExtendApiAnnotationTransform() {
    super("mule.revapi.api.ignoreNoExtend");
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

  @Override
  protected Map<String, DifferenceChecker> getDifferenceCheckers() {
    Map<String, DifferenceChecker> checkers = new HashMap<>();
    DifferenceChecker fieldChecker =
        (oldElement, newElement) -> isProtectedField(oldElement) && hasNoExtendAnnotation((TypeElement) oldElement.getParent());
    checkers.put(FIELD_REMOVED.code(), fieldChecker);
    checkers.put(FIELD_TYPE_CHANGED.code(), fieldChecker);

    DifferenceChecker methodChecker =
        (oldElement, newElement) -> isProtectedMethod(oldElement) && hasNoExtendAnnotation((TypeElement) oldElement.getParent());
    checkers.put(METHOD_REMOVED.code(), methodChecker);
    checkers.put(METHOD_NUMBER_OF_PARAMETERS_CHANGED.code(), methodChecker);
    checkers.put(METHOD_RETURN_TYPE_CHANGED.code(), methodChecker);

    DifferenceChecker methodAddedChecker = (oldElement, newElement) -> isProtectedConstructor(newElement)
        && hasNoExtendAnnotation((TypeElement) newElement.getParent());
    checkers.put(METHOD_ADDED.code(), methodAddedChecker);

    DifferenceChecker paramTypeChecker = (oldElement, newElement) -> isProtectedMethod(oldElement.getParent())
        && hasNoExtendAnnotation((TypeElement) oldElement.getParent().getParent());
    checkers.put(METHOD_PARAMETER_TYPE_CHANGED.code(), paramTypeChecker);

    return checkers;
  }

  private boolean hasNoExtendAnnotation(TypeElement typeElement) {
    return isTypeWithAnnotation(typeElement.getTypeEnvironment().getTypeUtils(), typeElement.getModelRepresentation(),
                                new ClassVisitor(NoExtend.class));
  }

  private boolean isProtectedMethod(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }

  private boolean isProtectedConstructor(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getKind().equals(CONSTRUCTOR)
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }

  private boolean isProtectedField(Element element) {
    return element instanceof FieldElement
        && ((FieldElement) element).getDeclaringElement().getModifiers().contains(PROTECTED);
  }
}
