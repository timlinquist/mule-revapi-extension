/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi.transform;

import static org.revapi.java.spi.Code.METHOD_ADDED_TO_INTERFACE;
import org.mule.api.annotation.NoImplement;

import java.util.HashMap;
import java.util.Map;

import org.revapi.java.model.TypeElement;

/**
 * Transforms API differences ignoring the ones that correspond to valid changes on interfaces annotated with {@link NoImplement}
 *
 * @since 1.1
 */
public class NoImplementApiAnnotationTransform extends AbstractApiAnnotationTransform {

  /**
   * Creates a new transformer
   */
  public NoImplementApiAnnotationTransform() {
    super("mule.revapi.api.ignoreNoImplement");
  }

  @Override
  protected String[] getDifferenceCodes() {
    return new String[] {METHOD_ADDED_TO_INTERFACE.code()};
  }

  @Override
  protected Map<String, DifferenceChecker> getDifferenceCheckers() {
    Map<String, DifferenceChecker> checkers = new HashMap<>();

    DifferenceChecker methodChecker =
        (oldElement, newElement) -> hasNoImplementAnnotation((TypeElement) newElement.getParent());
    checkers.put(METHOD_ADDED_TO_INTERFACE.code(), methodChecker);

    return checkers;
  }

  private boolean hasNoImplementAnnotation(TypeElement typeElement) {
    return isTypeWithAnnotation(typeElement.getTypeEnvironment().getTypeUtils(), typeElement.getModelRepresentation(),
                                new InterfaceVisitor(NoImplement.class));
  }
}
