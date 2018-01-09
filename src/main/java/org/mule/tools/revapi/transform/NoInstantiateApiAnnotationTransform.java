/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi.transform;

import static javax.lang.model.element.ElementKind.CONSTRUCTOR;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.revapi.java.spi.Code.METHOD_ADDED;
import static org.revapi.java.spi.Code.METHOD_NUMBER_OF_PARAMETERS_CHANGED;
import static org.revapi.java.spi.Code.METHOD_PARAMETER_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_REMOVED;
import org.mule.api.annotation.NoInstantiate;

import java.util.HashMap;
import java.util.Map;

import org.revapi.Element;
import org.revapi.java.model.MethodElement;
import org.revapi.java.model.TypeElement;

/**
 * Transforms API differences ignoring the ones that correspond to valid changes on classes annotated with {@link NoInstantiate}
 *
 * @since 1.1
 */
public class NoInstantiateApiAnnotationTransform extends AbstractApiAnnotationTransform {

  /**
   * Creates a new transformer
   */
  public NoInstantiateApiAnnotationTransform() {
    super("mule.revapi.api.ignoreNoInstantiate");
  }

  @Override
  protected String[] getDifferenceCodes() {
    return new String[] {
        METHOD_REMOVED.code(),
        METHOD_ADDED.code(),
        METHOD_NUMBER_OF_PARAMETERS_CHANGED.code(),
        METHOD_PARAMETER_TYPE_CHANGED.code()
    };
  }

  @Override
  protected Map<String, DifferenceChecker> getDifferenceCheckers() {
    Map<String, DifferenceChecker> checkers = new HashMap<>();

    DifferenceChecker methodChecker =
        (oldElement, newElement) -> isPublicConstructor(oldElement)
            && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent());
    checkers.put(METHOD_REMOVED.code(), methodChecker);
    checkers.put(METHOD_NUMBER_OF_PARAMETERS_CHANGED.code(), methodChecker);

    DifferenceChecker methodAddedChecker = (oldElement, newElement) -> isPublicConstructor(newElement)
        && hasNoInstantiateAnnotation((TypeElement) newElement.getParent());
    checkers.put(METHOD_ADDED.code(), methodAddedChecker);

    DifferenceChecker paramTypeChecker = (oldElement, newElement) -> isPublicConstructor(oldElement.getParent())
        && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent().getParent());
    checkers.put(METHOD_PARAMETER_TYPE_CHANGED.code(), paramTypeChecker);

    return checkers;
  }

  private boolean hasNoInstantiateAnnotation(TypeElement typeElement) {
    return isTypeWithAnnotation(typeElement.getTypeEnvironment().getTypeUtils(), typeElement.getModelRepresentation(),
                                new ClassVisitor(NoInstantiate.class));
  }

  private boolean isPublicConstructor(Element element) {
    return element instanceof MethodElement
        && ((MethodElement) element).getDeclaringElement().getKind().equals(CONSTRUCTOR)
        && ((MethodElement) element).getDeclaringElement().getModifiers().contains(PUBLIC);
  }
}
