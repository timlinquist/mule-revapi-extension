/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.lang.String.format;

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

  private ApiBoundary getApiBoundary(Element<?> element, Configuration configuration) {
    ApiBoundary apiBoundary =
        new CachedApiBoundary(configuration.getModuleSystemStrategy().getApiBoundary(element, configuration));
    if (isVerboseLogging()) {
      LOG.info(apiBoundary.toString());
    }
    return apiBoundary;
  }

  /**
   * Determines if an {@link Element} is part of the API.
   * 
   * @param element The element that must be checked.
   * @return True if the element is part of the API.
   */
  private boolean isApiElement(Element<?> element) {
    boolean isApi = false;
    if (apiBoundaries.containsKey(element.getArchive())) {
      isApi = apiBoundaries.computeIfAbsent(element.getArchive(), archive -> getApiBoundary(element, configuration))
          .isApi(element);
    }
    if (isVerboseLogging()) {
      logIsApi(element, isApi);
    }
    return isApi;
  }

  /**
   * Logs whether an element is part of an API.
   * 
   * @param element The element.
   * @param isApi   True if the element is part of the API.
   */
  private void logIsApi(Element<?> element, boolean isApi) {
    LOG.info("{} is {}", element, isApi ? "part of the API" : "NOT part of the API");
  }

  /**
   * Filter configuration POJO. Will be instantiated by parsing the filter configuration.
   */
  private static class Configuration {

    private List<String> jpmsExcludedTargets = Collections.emptyList();
    private ModuleSystemStrategy moduleSystemStrategy = ModuleSystemStrategy.MIXED;

    public Configuration() {}

    /**
     * @return A List of regular expressions. Any jpms target module whose fully qualified name matches an expression defined here
     *         will not be taken into account when defining the module API. This is useful to exclude directed exports to modules
     *         that can tolerate breaking changes via refactor.
     */
    public List<String> getJpmsExcludedTargets() {
      return jpmsExcludedTargets;
    }

    /**
     * @return One of the following module system strategies:</br>
     *         MIXED: Prioritizes JPMS but will try to switch to MMS if the JPMS module is an automatic one.</br>
     *         JAVA: JPMS Only.</br>
     *         MULE: MMS only.</br>
     */
    public ModuleSystemStrategy getModuleSystemStrategy() {
      return moduleSystemStrategy;
    }

    public void setJpmsExcludedTargets(List<String> jpmsExcludedTargets) {
      this.jpmsExcludedTargets = jpmsExcludedTargets;
    }

    public void setModuleSystemStrategy(String moduleSystemStrategy) {
      this.moduleSystemStrategy = ModuleSystemStrategy.valueOf(moduleSystemStrategy);
    }
  }

  /**
   * Enumeration of the possible module system strategies: MIXED: Prioritizes JPMS but will try to switch to MMS if the JPMS
   * module is an automatic one.</br>
   * JAVA: JPMS Only.</br>
   * MULE: MMS only.</br>
   * Strategies can create {@link ApiBoundary} instances via {@link #getApiBoundary(Element, Configuration)}.
   */
  private enum ModuleSystemStrategy {

    MIXED() {

      @Override
      public ApiBoundary getApiBoundary(Element<?> element, Configuration configuration) {
        JavaModuleSystemApiBoundary jpmsApiBoundary = (JavaModuleSystemApiBoundary) JAVA.getApiBoundary(element, configuration);
        // Automatic modules must prioritize MuleModuleSystem descriptors when mode is MIXED.
        if (jpmsApiBoundary.isAutomatic()) {
          ApiBoundary muleModuleSystemApiBoundary = new MuleModuleSystemApiBoundary(element.getArchive());
          if (!muleModuleSystemApiBoundary.isEmpty()) {
            return muleModuleSystemApiBoundary;
          }
        }
        return jpmsApiBoundary;
      }
    },
    JAVA {

      @Override
      public JavaModuleSystemApiBoundary getApiBoundary(Element<?> element, Configuration configuration) {
        return new JavaModuleSystemApiBoundary((JavaModelElement) element, configuration.getJpmsExcludedTargets());
      }
    },
    MULE {

      @Override
      public ApiBoundary getApiBoundary(Element<?> element, Configuration configuration) {
        return new MuleModuleSystemApiBoundary(element.getArchive());
      }
    };

    /**
     * Creates an {@link ApiBoundary} instance.
     * 
     * @param element       Element that will be used to determine the boundary.
     * @param configuration This filter configuration.
     * @return ApiBoundary that corresponds to the provided element.
     */
    public abstract ApiBoundary getApiBoundary(Element<?> element, Configuration configuration);
  }
}
