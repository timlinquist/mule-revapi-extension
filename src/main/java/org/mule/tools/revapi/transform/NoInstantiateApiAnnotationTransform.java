/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi.transform;

import static org.revapi.java.spi.Code.FIELD_REMOVED;
import static org.revapi.java.spi.Code.FIELD_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_ADDED;
import static org.revapi.java.spi.Code.METHOD_NUMBER_OF_PARAMETERS_CHANGED;
import static org.revapi.java.spi.Code.METHOD_PARAMETER_TYPE_CHANGED;
import static org.revapi.java.spi.Code.METHOD_REMOVED;
import static org.revapi.java.spi.Code.METHOD_RETURN_TYPE_CHANGED;
import org.mule.api.annotation.NoInstantiate;

import java.util.HashMap;
import java.util.Map;

import org.revapi.java.model.TypeElement;

/**
 * Transforms API differences ignoring the ones that correspond to valid changes on classes annotated with {@link NoInstantiate}
 *
 * @since 1.1
 */
public class NoInstantiateApiAnnotationTransform extends AbstractClassApiAnnotationTransform {

  /**
   * Creates a new transformer
   */
  public NoInstantiateApiAnnotationTransform() {
    super("mule.revapi.api.ignoreNoInstantiate");
  }

  @Override
  protected Map<String, DifferenceChecker> getDifferenceCheckers() {
    Map<String, DifferenceChecker> checkers = new HashMap<>();

    DifferenceChecker fieldChecker =
        (oldElement, newElement) -> isProtectedField(oldElement)
            && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent());
    checkers.put(FIELD_REMOVED.code(), fieldChecker);
    checkers.put(FIELD_TYPE_CHANGED.code(), fieldChecker);

    DifferenceChecker methodChecker =
        (oldElement,
         newElement) -> isPublicConstructor(oldElement) && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent())
             || isProtectedMethod(oldElement) && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent());
    checkers.put(METHOD_REMOVED.code(), methodChecker);
    checkers.put(METHOD_NUMBER_OF_PARAMETERS_CHANGED.code(), methodChecker);
    checkers.put(METHOD_RETURN_TYPE_CHANGED.code(), methodChecker);

    DifferenceChecker methodAddedChecker = (oldElement, newElement) -> isPublicConstructor(newElement)
        && hasNoInstantiateAnnotation((TypeElement) newElement.getParent())
        || isProtectedConstructor(newElement) && hasNoInstantiateAnnotation((TypeElement) newElement.getParent());
    checkers.put(METHOD_ADDED.code(), methodAddedChecker);

    DifferenceChecker paramTypeChecker = (oldElement, newElement) -> isPublicConstructor(oldElement.getParent())
        && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent().getParent())
        || isProtectedMethod(oldElement.getParent())
            && hasNoInstantiateAnnotation((TypeElement) oldElement.getParent().getParent());
    checkers.put(METHOD_PARAMETER_TYPE_CHANGED.code(), paramTypeChecker);

    return checkers;
  }

  private boolean hasNoInstantiateAnnotation(TypeElement typeElement) {
    return isTypeWithAnnotation(typeElement.getTypeEnvironment().getTypeUtils(), typeElement.getModelRepresentation(),
                                new ClassVisitor(NoInstantiate.class));
  }

}
