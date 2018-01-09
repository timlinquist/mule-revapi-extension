/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi.transform;

import static javax.lang.model.element.ElementKind.INTERFACE;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.SimpleTypeVisitor8;

/**
 * Visitor that accepts interfaces annotated with a given annotation class
 *
 * @since 1.1
 */
class InterfaceVisitor extends SimpleTypeVisitor8<Boolean, Void> {

  private final Class annotationClass;

  /**
   * Creates a new visitor
   *
   * @param annotationClass annotation that will be accepted by the visitor
   */
  InterfaceVisitor(Class annotationClass) {
    super(false);
    this.annotationClass = annotationClass;
  }

  @Override
  public Boolean visitDeclared(DeclaredType t, Void aVoid) {
    return t.asElement().getKind() == INTERFACE && t.asElement().getAnnotation(annotationClass) != null;
  }
}
