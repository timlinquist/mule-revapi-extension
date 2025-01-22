/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import org.revapi.java.spi.JavaTypeElement;

import java.util.HashMap;
import java.util.Map;

public class CachedApiBoundary implements ApiBoundary {

  private final ApiBoundary delegate;
  private final Map<JavaTypeElement, Boolean> isApiCache = new HashMap<>();

  public CachedApiBoundary(ApiBoundary delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean isApi(JavaTypeElement element) {
    if (isApiCache.containsKey(element)) {
      return isApiCache.get(element);
    } else {
      final boolean isApi = delegate.isApi(element);
      isApiCache.put(element, isApi);
      return isApi;
    }
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public String toString() {
    return delegate.toString();
  }
}
