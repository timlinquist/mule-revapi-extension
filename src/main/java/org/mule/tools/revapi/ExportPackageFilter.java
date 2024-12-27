/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import org.revapi.API;
import org.revapi.AnalysisContext;
import org.revapi.Archive;
import org.revapi.Element;
import org.revapi.ElementFilter;
import org.revapi.java.model.TypeElement;
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

  private static final Logger LOG = LoggerFactory.getLogger(ExportPackageFilter.class);

  private final Map<API, Set<ExportedPackages>> apiModulesExportedPackages = new HashMap<>();
  private final Map<Element<?>, Boolean> apiExportedElements = new HashMap<>();

  private final boolean isMixedMode = false;

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
    apiModulesExportedPackages.computeIfAbsent(analysisContext.getOldApi(), this::getExportedPackages);
    apiModulesExportedPackages.computeIfAbsent(analysisContext.getNewApi(), this::getExportedPackages);
  }

  @Override
  public boolean applies(Element element) {
    boolean exported;
    if (element instanceof JavaTypeElement) {
      exported = isExported(element);
    } else {
      TypeElement ownerJavaTypeElement = findOwnerJavaTypeElement(element);
      exported = isExported(ownerJavaTypeElement);
    }
    if (isVerboseLogging()) {
      logIsExported(element, exported);
    }
    return exported;
  }

  @Override
  public boolean shouldDescendInto(Object element) {
    boolean descendInto = element instanceof Element && isExported((Element<?>) element);
    if (isVerboseLogging()) {
      LOG.info("{}: should descend into {}", descendInto, element);
    }
    return descendInto;
  }

  private Set<ExportedPackages> getExportedPackages(API api) {
    Set<ExportedPackages> exportedPackages = new HashSet<>();
    api.getArchives().forEach(archive -> {
      if (!addJavaModuleSystemExportedPackages(archive, exportedPackages)) {
        if (!addMuleModuleSystemExportedPackages(archive, exportedPackages)) {
          LOG.debug("No exported packages found for the archive {}.", archive.getName());
        }
      }
    });
    if (exportedPackages.isEmpty()) {
      LOG.debug("No exported packages found for the API {}.", api);
    }
    return exportedPackages;
  }

  private boolean addMuleModuleSystemExportedPackages(Archive archive, Set<ExportedPackages> exportedPackages) {
    try (JarInputStream jarFile = new JarInputStream(archive.openStream())) {
      JarEntry entry;
      while ((entry = jarFile.getNextJarEntry()) != null) {
        String name = entry.getName();
        if (name.equals("META-INF/mule-module.properties")) {
          Properties properties = getProperties(jarFile);
          ExportedPackages muleModuleSystemExportedPackages = new MuleModuleSystemExportedPackages(properties);
          if (isVerboseLogging()) {
            muleModuleSystemExportedPackages.logExportedPackages();
          }
          exportedPackages.add(muleModuleSystemExportedPackages);
          return true;
        }
      }
    } catch (IOException e) {
      LOG.debug("Failed to open the archive {} as a jar.", archive.getName(), e);
    }
    LOG.debug("No Mule Module System descriptor found for the archive {}.", archive.getName());
    return false;
  }

  private boolean addJavaModuleSystemExportedPackages(Archive archive, Set<ExportedPackages> exportedPackages) {
    Optional<ModuleReference> moduleReference = findModule(archive);
    if (moduleReference.isPresent()) {
      ModuleReference module = moduleReference.get();
      // TODO: Evaluate introducing config modes: MIXED: JPMS with MMS as fallback, JPMS: JPMS only, MULE: MMS only.
      if (!module.descriptor().isAutomatic()
          // Automatic modules must prioritize MuleModuleSystem descriptors.
          || (isMixedMode && !addMuleModuleSystemExportedPackages(archive, exportedPackages))) {
        ExportedPackages javaModuleSystemExportedPackages = new JavaModuleSystemExportedPackages(module.descriptor());
        if (isVerboseLogging()) {
          javaModuleSystemExportedPackages.logExportedPackages();
        }
        exportedPackages.add(javaModuleSystemExportedPackages);
      }
      return true;
    } else {
      LOG.debug("No Java Module System descriptor found for the archive: {}.", archive.getName());
      return false;
    }
  }

  private TypeElement findOwnerJavaTypeElement(Element<?> element) {
    while (!(element instanceof JavaTypeElement) || element.getParent() instanceof TypeElement) {
      element = element.getParent();
    }
    if (!(element instanceof JavaTypeElement)) {
      throw new IllegalStateException("Cannot find the parent type element for: " + element.getFullHumanReadableString());
    }
    return (TypeElement) element;
  }

  private boolean isExported(Element<?> element) {
    if (apiExportedElements.containsKey(element)) {
      return apiExportedElements.get(element);
    }
    boolean exported;
    if (!(element instanceof TypeElement)) {
      exported = false;
    } else {
      String packageName = getPackageName((TypeElement) element);
      exported = apiModulesExportedPackages.get(element.getApi()).stream()
          .anyMatch(exportedPackages -> exportedPackages.isExported(packageName));
    }
    if (isVerboseLogging()) {
      logIsExported(element, exported);
    }
    apiExportedElements.put(element, exported);
    return exported;
  }

  private Optional<ModuleReference> findModule(Archive archive) {
    try {
      Field archiveFile = archive.getClass().getDeclaredField("file");
      archiveFile.setAccessible(true);
      ModuleFinder moduleFinder = ModuleFinder.of(((File) archiveFile.get(archive)).toPath());
      Set<ModuleReference> moduleReferences = moduleFinder.findAll();

      if (moduleReferences.size() > 1) {
        throw new IllegalArgumentException("More than one module found for the archive " + archive.getName() + ": "
            + getModuleNames(moduleReferences));
      }

      if (moduleReferences.isEmpty()) {
        return Optional.empty();
      } else {
        return Optional.of(moduleReferences.iterator().next());
      }
    } catch (Exception e) {
      LOG.error("Error while finding modules for archive: {}", archive.getName(), e);
      throw new RuntimeException(e);
    }
  }

  private Properties getProperties(JarInputStream propertiesFile) throws IOException {
    Properties properties = new Properties();
    byte[] bytes = getBytes(new BufferedInputStream(propertiesFile));
    properties.load(new ByteArrayInputStream(bytes));
    return properties;
  }

  private String getPackageName(TypeElement element) {
    String canonicalName = findOwnerJavaTypeElement(element).getCanonicalName();
    int index = canonicalName.lastIndexOf(".");
    return canonicalName.substring(0, index);
  }

  private byte[] getBytes(InputStream is)
      throws IOException {
    byte[] buffer = new byte[8192];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
    int n;
    while ((n = is.read(buffer, 0, buffer.length)) != -1) {
      outputStream.write(buffer, 0, n);
    }
    return outputStream.toByteArray();
  }

  private boolean isMixedMode() {
    return isMixedMode;
  }

  private String getModuleNames(Set<ModuleReference> moduleReferences) {
    return moduleReferences.stream().map(moduleReference -> moduleReference.descriptor().name())
        .collect(Collectors.joining(", "));
  }

  private void logIsExported(Element<?> element, boolean exported) {
    LOG.info("{} : applies to {}", exported, element);
  }
}
