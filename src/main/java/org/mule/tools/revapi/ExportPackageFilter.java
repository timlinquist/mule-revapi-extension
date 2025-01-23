/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.revapi.AnalysisContext;
import org.revapi.Archive;
import org.revapi.Element;
import org.revapi.ElementFilter;
import org.revapi.java.spi.JavaModelElement;
import org.revapi.java.spi.JavaTypeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filters elements that are not part of a given Mule module API, so the API modification checks are not executed on them.
 * <p/>
 * This filter considers both standard and privileged APIs by merging them into a single API.
 *
 * @since 1.0
 */
public final class ExportPackageFilter implements ElementFilter {

  private static final ObjectMapper CONFIG_MAPPER =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  private static final Logger LOG = LoggerFactory.getLogger(ExportPackageFilter.class);
  private final Map<Archive, ApiBoundary> apiBoundaries = new HashMap<>();
  private Configuration configuration;

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
    initializeApiBoundaries(analysisContext);
    configuration = readConfiguration(analysisContext);
  }

  /**
   * This is done in order to validate only elements that are part of API archives and not, for example, Java JDK elements.
   * 
   * @param analysisContext This filter's {@link AnalysisContext}.
   */
  private void initializeApiBoundaries(AnalysisContext analysisContext) {
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

  private Configuration readConfiguration(AnalysisContext analysisContext) {
    try {
      Configuration configuration =
          CONFIG_MAPPER.treeToValue(analysisContext.getConfigurationNode(), Configuration.class);
      return configuration == null ? new Configuration() : configuration;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(format("Could not read the filter configuration for [%s].", this.getExtensionId()), e);
    }
  }

  private ApiBoundary getApiBoundaries(Element<?> element) {
    ApiBoundary apiBoundary;
    if (isJavaMode() || isMixedMode()) {
      apiBoundary = getJavaModuleSystemApiBoundary(element);
    } else {
      apiBoundary = getMuleModuleSystemApiBoundary(element.getArchive());
    }
    if (isVerboseLogging()) {
      LOG.info(apiBoundary.toString());
    }
    return apiBoundary;
  }

  private ApiBoundary getMuleModuleSystemApiBoundary(Archive archive) {
    requireNonNull(archive, "Archive must not be null.");
    if (isMixedMode() || isMuleMode()) {
      return new CachedApiBoundary(new MuleModuleSystemApiBoundary(archive));
    } else {
      throw new IllegalArgumentException("Mule Module System API boundaries are not supported for the Java Module System mode.");
    }
  }

  private ApiBoundary getJavaModuleSystemApiBoundary(Element<?> element) {
    if (isJavaMode() || isMixedMode()) {
      JavaModuleSystemApiBoundary jpmsApiBoundary =
          new JavaModuleSystemApiBoundary((JavaModelElement) element, configuration.getJpmsExcludedTargets());
      // Automatic modules must prioritize MuleModuleSystem descriptors when mode is MIXED.
      if (jpmsApiBoundary.isAutomatic() && isMixedMode()) {
        ApiBoundary muleModuleSystemApiBoundary = getMuleModuleSystemApiBoundary(element.getArchive());
        if (!muleModuleSystemApiBoundary.isEmpty()) {
          return new CachedApiBoundary(muleModuleSystemApiBoundary);
        }
      }
      return new CachedApiBoundary(jpmsApiBoundary);
    } else {
      throw new IllegalArgumentException("Java Module System API boundaries are not supported for the Mule Module System mode.");
    }
  }

  /**
   * Determines if an {@link Element} is part of the API.
   * 
   * @param element The element that must be checked.
   * @return True if the element is part of the API.
   */
  private boolean isApiElement(Element<?> element) {
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

  /**
   * Filter configuration POJO. Will be instantiated by parsing the filter configuration.
   */
  private static class Configuration {

    /**
     * List of regular expressions. Any jpms target module whose fully qualified name matches an expression defined here will not
     * be taken into account when defining the module API. This is useful to exclude directed exports to modules that should be
     * refactored if breaking changes happen.
     */
    private List<String> jpmsExcludedTargets = Collections.emptyList();

    public Configuration() {}

    public List<String> getJpmsExcludedTargets() {
      return jpmsExcludedTargets;
    }

    public void setJpmsExcludedTargets(List<String> jpmsExcludedTargets) {
      this.jpmsExcludedTargets = jpmsExcludedTargets;
    }
  }
}
