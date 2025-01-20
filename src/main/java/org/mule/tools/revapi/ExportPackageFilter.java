/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.revapi.AnalysisContext;
import org.revapi.Archive;
import org.revapi.Element;
import org.revapi.ElementFilter;
import org.revapi.java.spi.JavaModelElement;
import org.revapi.java.spi.JavaTypeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Filters elements that are not part of a given Mule module API, so the API modification checks are not executed on them.
 * <p/>
 * This filter considers both standard and privileged APIs by merging them into a single API.
 *
 * @since 1.0
 */
public final class ExportPackageFilter implements ElementFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ExportPackageFilter.class);

  private final Map<Archive, ApiBoundary> apiBoundaries = new HashMap<>();
  private final Map<Element<?>, Boolean> isApiElement = new HashMap<>();
  private final List<Integer> hashes = new ArrayList<>();

  @Override
  public void close() {}

  private static boolean isVerboseLogging() {
    return System.getProperty("mule.revapi.verbose") != null;
  }

  private static String getModuleSystemMode() {
    return System.getProperty("mule.revapi.moduleSystem.mode", "MIXED");
  }

  @Override
  public String getExtensionId() {
    return "mule.module.filter";
  }

  @Override
  public Reader getJSONSchema() {
    return null;
  }

  @Override
  public void initialize(AnalysisContext analysisContext) {
    // This is done in order to validate only the elements corresponding to the API archives. The boundaries will be lazily
    // computed.
    analysisContext.getOldApi().getArchives().forEach(archive -> apiBoundaries.put(archive, null));
    analysisContext.getNewApi().getArchives().forEach(archive -> apiBoundaries.put(archive, null));
  }

  @Override
  public boolean applies(Element element) {
    return isApiElement(element);
  }

  @Override
  public boolean shouldDescendInto(Object element) {
    boolean descendInto = element instanceof JavaTypeElement && isApiElement((JavaTypeElement) element);
    if (isVerboseLogging()) {
      LOG.info("Filter {} descend into {}", descendInto ? "Will" : "Will NOT", element);
    }
    return descendInto;
  }

  private ApiBoundary getApiBoundaries(Element<?> element) {
    ApiBoundary apiBoundary;
    if (isJavaMode() || isMixedMode()) {
      apiBoundary = getJavaModuleSystemApiBoundary(element);
    } else {
      apiBoundary = getMuleModuleSystemApiBoundary(element.getArchive());
    }
    if (isVerboseLogging()) {
      // TODO: Replace by a toString in the ApiBoundary and log it here.
      apiBoundary.logApiPackages();
    }
    return apiBoundary;
  }

  private ApiBoundary getMuleModuleSystemApiBoundary(Archive archive) {
    requireNonNull(archive, "Archive must not be null.");
    if (isMixedMode() || isMuleMode()) {
      return new MuleModuleSystemApiBoundary(archive);
    } else {
      throw new IllegalArgumentException("Mule Module System API boundaries are not supported for the Java Module System mode.");
    }
  }

  private ApiBoundary getJavaModuleSystemApiBoundary(Element<?> element) {
    if (isJavaMode() || isMixedMode()) {
      JavaModuleSystemApiBoundary jpmsApiBoundary = new JavaModuleSystemApiBoundary((JavaModelElement) element);
      // Automatic modules must prioritize MuleModuleSystem descriptors when mode is MIXED.
      if (jpmsApiBoundary.isOpen() && isMixedMode()) {
        ApiBoundary muleModuleSystemApiBoundary = getMuleModuleSystemApiBoundary(element.getArchive());
        if (!muleModuleSystemApiBoundary.isEmpty()) {
          return muleModuleSystemApiBoundary;
        }
      }
      return jpmsApiBoundary;
    } else {
      throw new IllegalArgumentException("Java Module System API boundaries are not supported for the Mule Module System mode.");
    }
  }

  private boolean isApiElement(Element<?> element) {

    // synchronized (isApiElement) {
    // if (isApiElement.containsKey(element.toString())) {
    // return isApiElement.get(element.toString());
    // }
    // }

    boolean isApi = false;
    synchronized (apiBoundaries) {
      if (apiBoundaries.containsKey(element.getArchive())) {
        isApi = apiBoundaries.computeIfAbsent(element.getArchive(), archive -> getApiBoundaries(element))
            .isApi(element);
      }
    }
    if (isVerboseLogging()) {
      logIsApi(element, isApi);
    }

    // synchronized (isApiElement) {
    // isApiElement.put(element, isApi);
    // }

    return isApi;
  }

  private boolean isMixedMode() {
    return getModuleSystemMode().equals("MIXED");
  }

  private boolean isMuleMode() {
    return getModuleSystemMode().equals("MULE");
  }

  private boolean isJavaMode() {
    return getModuleSystemMode().equals("JAVA");
  }

  private void logIsApi(Element<?> element, boolean isApi) {
    LOG.info("{} is {}", element, isApi ? "part of the API" : "NOT part of the API");
  }
}
