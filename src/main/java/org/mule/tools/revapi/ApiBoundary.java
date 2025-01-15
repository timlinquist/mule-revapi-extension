/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import org.revapi.Element;
import org.revapi.java.model.TypeElement;

import static java.util.Objects.requireNonNull;

public interface ApiBoundary {

  /**
   * @param element The element that is going to be checked.
   * @return True if the element is part of the API.
   */
  default boolean isApi(Element<?> element) {
    return isApi(findJavaTypeElement(element));
  }

  default String getPackageName(TypeElement element) {
    String canonicalName = element.getCanonicalName();
    int index = canonicalName.lastIndexOf(".");
    return canonicalName.substring(0, index);
  }

  boolean isApi(TypeElement element);

  /**
   * @return true if any valid call to {@link #isApi(Element)} or {@link #isApi(TypeElement)} will return false.
   */
  boolean isEmpty();

  void logApiPackages();

  /**
   * Walks the {@link Element} hierarchy up in order to find the first {@link TypeElement}, which represents a Java class.
   * 
   * @param element The element that will be walked. Will be returned right away if it is a {@link TypeElement}.
   * @return The first {@link TypeElement} found.
   * @throws IllegalStateException If no {@link TypeElement} could be found.
   */
  private TypeElement findJavaTypeElement(Element<?> element) {
    requireNonNull(element, "Element must not be null.");
    Element<?> typeElement = element;
    while (typeElement != null && (!(typeElement instanceof TypeElement) || typeElement.getParent() instanceof TypeElement)) {
      typeElement = typeElement.getParent();
    }
    if (typeElement == null) {
      throw new IllegalStateException("Could not find the Java Type element for: " + element.getFullHumanReadableString());
    }
    return (TypeElement) typeElement;
  }
}
