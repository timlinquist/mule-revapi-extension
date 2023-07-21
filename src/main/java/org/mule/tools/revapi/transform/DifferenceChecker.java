/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi.transform;

import org.revapi.Element;

/**
 * Decides whether or not a given API difference must be ignored.
 *
 * @since 1.1
 */
interface DifferenceChecker {

  /**
   * Indicates if a given different can be ignored
   *
   * @param oldElement element from the old version of the API. Can be null
   * @param newElement element form the new version of the API. Can be null
   * @return true if the difference can be ignored, false otherwise.
   */
  boolean ignore(Element oldElement, Element newElement);
}
