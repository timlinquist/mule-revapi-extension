/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.util.Objects.requireNonNull;

import org.revapi.Element;
import org.revapi.java.spi.JavaTypeElement;

public interface ApiBoundary {

  /**
   * @param element The element that is going to be checked.
   * @return True if the element is part of the API.
   */
  default boolean isApi(Element<?> element) {
    return isApi(findParentJavaTypeElement(element));
  }

  /**
   * @param element The element that is going to be checked.
   * @return True if the element is part of the API.
   */
  boolean isApi(JavaTypeElement element);

  /**
   * @return true if any valid call to {@link #isApi(Element)} or {@link #isApi(JavaTypeElement)} will return false.
   */
  boolean isEmpty();

  default String getPackageName(JavaTypeElement element) {
    String canonicalName = element.getDeclaringElement().getQualifiedName().toString();
    int index = canonicalName.lastIndexOf(".");
    return canonicalName.substring(0, index);
  }

  /**
   * Walks the {@link Element} hierarchy up in order to return the top parent {@link JavaTypeElement} found.
   * 
   * @param element The element that will be walked.
   * @return The top {@link JavaTypeElement} found.
   * @throws IllegalStateException If no {@link JavaTypeElement} could be found.
   */
  private static JavaTypeElement findParentJavaTypeElement(Element<?> element) {
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
