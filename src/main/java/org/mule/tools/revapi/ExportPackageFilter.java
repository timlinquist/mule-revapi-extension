/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.revapi;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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
 */
public final class ExportPackageFilter implements ElementFilter {

  private static final String EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.export.classPackages";
  private static final String PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.privileged.classPackages";
  private static final Logger LOG = LoggerFactory.getLogger(ExportPackageFilter.class);

  private Map<API, Set<String>> exportedPackages;
  private Map<Element, Boolean> exportedElements = new HashMap();

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
    exportedPackages = new HashMap<>();
    Function<API, Set<String>> getExportedPackages = api -> {
      Set<String> exportedPackages = new HashSet<>();
      api.getArchives().forEach(a -> addExportedPackages(a, exportedPackages));
      return exportedPackages;

    };

    exportedPackages.computeIfAbsent(analysisContext.getOldApi(), getExportedPackages);
    exportedPackages.computeIfAbsent(analysisContext.getNewApi(), getExportedPackages);
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
      LOG.info(exported + " : applies to " + element);
    }

    return exported;
  }

  @Override
  public boolean shouldDescendInto(Object element) {
    boolean descendInto = element instanceof Element ? isExported((Element) element) : false;

    if (isVerboseLogging()) {
      LOG.info(descendInto + ": should descend into " + element);
    }

    return descendInto;
  }

  private TypeElement findOwnerJavaTypeElement(Element element) {
    while (!(element instanceof JavaTypeElement) || element.getParent() instanceof TypeElement) {
      element = element.getParent();
    }

    if (!(element instanceof JavaTypeElement)) {
      throw new IllegalStateException("Cannot find the parent type element for: " + element);
    }

    return (TypeElement) element;
  }

  private boolean isExported(Element element) {
    if (exportedElements.containsKey(element)) {
      return exportedElements.get(element);
    }

    boolean exported;
    if (!(element instanceof TypeElement)) {
      exported = false;
    } else {
      String packageName = getPackageName((TypeElement) element);
      Set<String> exportDefinitions = exportedPackages.get(element.getApi());
      if (exportDefinitions == null || exportDefinitions.isEmpty()) {
        exported = false;
      } else {
        exported = exportDefinitions.contains(packageName);
      }
    }
    if (isVerboseLogging()) {
      LOG.info(exported + " : applies to " + element);
    }
    exportedElements.put(element, exported);
    return exported;
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

  private void addExportedPackages(Archive archive, Set<String> exportedPackages) {
    try (JarInputStream jarFile = new JarInputStream(archive.openStream())) {
      JarEntry entry;

      while ((entry = jarFile.getNextJarEntry()) != null) {
        String name = entry.getName();

        if (name.equals("META-INF/mule-module.properties")) {
          Properties properties = new Properties();
          byte bytes[] = getBytes(new BufferedInputStream(jarFile));
          properties.load(new ByteArrayInputStream(bytes));

          Set<String> standardPackages = getPackagesFromProperty(properties, EXPORTED_CLASS_PACKAGES_PROPERTY);
          exportedPackages.addAll(standardPackages);
          Set<String> privilegedPackages = getPackagesFromProperty(properties, PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY);
          exportedPackages.addAll(privilegedPackages);

          if (isVerboseLogging()) {
            LOG.info("Adding exported packages from: " + jarFile + "\nstandard: " + standardPackages + "\nprivileged: "
                + privilegedPackages);
          }
        }
      }
    } catch (IOException e) {
      LOG.debug("Failed to open the archive " + archive + " as a jar.", e);
    }
  }

  private Set<String> getPackagesFromProperty(Properties properties, String propertyName) {
    Set<String> result = new HashSet<>();
    String property = properties.getProperty(propertyName);
    if (property != null) {
      String[] packages = property.split(",");
      for (String packageName : packages) {
        String name = packageName.trim();
        if (!"".equals(name)) {
          result.add(name);
        }
      }
    }

    return result;
  }
}
