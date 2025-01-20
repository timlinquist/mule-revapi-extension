/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import org.revapi.Element;
import org.revapi.java.model.TypeElement;
import org.revapi.java.spi.JavaTypeElement;

import static java.util.Objects.requireNonNull;

public interface ApiBoundary {

  /**
   * @param element The element that is going to be checked.
   * @return True if the element is part of the API.
   */
  default boolean isApi(Element<?> element) {
    Element<?> ownerElement = element;
    if (!(ownerElement instanceof JavaTypeElement)) {
      ownerElement = ApiBoundary.findJavaTypeElement(element);
    }
    JavaTypeElement finalElement = (JavaTypeElement) ownerElement;
    return isApi(finalElement);
  }

  boolean isApi(JavaTypeElement element);

  /**
   * @return true if any valid call to {@link #isApi(Element)} or {@link #isApi(TypeElement)} will return false.
   */
  boolean isEmpty();

  void logApiPackages();

  default String getPackageName(JavaTypeElement element) {
    String canonicalName = element.getDeclaringElement().getQualifiedName().toString();
    int index = canonicalName.lastIndexOf(".");
    return canonicalName.substring(0, index);
  }

  /**
   * Walks the {@link Element} hierarchy up in order to find the first {@link TypeElement}, which represents a Java class.
   *
   * @param element The element that will be walked. Will be returned right away if it is a {@link TypeElement}.
   * @return The first {@link TypeElement} found.
   * @throws IllegalStateException If no {@link TypeElement} could be found.
   */
  private static JavaTypeElement findJavaTypeElement(Element<?> element) {
    requireNonNull(element, "Element must not be null.");
    Element<?> typeElement = element;
    while (typeElement != null
        && (!(typeElement instanceof JavaTypeElement) || typeElement.getParent() instanceof JavaTypeElement)) {
      typeElement = typeElement.getParent();
    }
    if (typeElement == null) {
      throw new IllegalStateException("Could not find the Java Type element for: " + element.getFullHumanReadableString());
    }
    return (JavaTypeElement) typeElement;
  }

}
