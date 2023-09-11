/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi.transform;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.ENUM;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.SimpleTypeVisitor8;

/**
 * Visitor that accepts classes annotated with a given annotation class
 *
 * @since 1.1
 */
class ClassVisitor extends SimpleTypeVisitor8<Boolean, Void> {

  private final Class annotationClass;

  /**
   * Creates a new visitor
   *
   * @param annotationClass annotation that will be accepted by the visitor
   */
  public ClassVisitor(Class annotationClass) {
    super(false);
    this.annotationClass = annotationClass;
  }

  @Override
  public Boolean visitDeclared(DeclaredType t, Void aVoid) {
    return (t.asElement().getKind() == CLASS || t.asElement().getKind() == ENUM)
        && t.asElement().getAnnotation(annotationClass) != null;
  }
}
