/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.lang.String.format;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.revapi.Archive;
import org.revapi.java.spi.JavaTypeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleModuleSystemApiBoundary implements ApiBoundary {

  private static final String MULE_MODULE_NAME_PROPERTY = "module.name";
  private static final String EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.export.classPackages";
  private static final String PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.privileged.classPackages";

  private static final Logger LOG = LoggerFactory.getLogger(MuleModuleSystemApiBoundary.class);

  private final Set<String> standardPackages = new HashSet<>();
  private final Set<String> privilegedPackages = new HashSet<>();
  private String moduleName;


  private String getModuleName(Properties muleModuleSystemProperties) {
    return muleModuleSystemProperties.getProperty(MULE_MODULE_NAME_PROPERTY);
  }

  public MuleModuleSystemApiBoundary(Archive archive) {
    try (JarInputStream jarFile = new JarInputStream(archive.openStream())) {
      JarEntry entry;
      while ((entry = jarFile.getNextJarEntry()) != null) {
        String name = entry.getName();
        if (name.equals("META-INF/mule-module.properties")) {
          Properties properties = getProperties(jarFile);
          moduleName = getModuleName(properties);
          loadExportedPackages(properties);
          break;
        }
      }
    } catch (IOException e) {
      LOG.error("Failed to open the archive {} as a jar.", archive.getName(), e);
      throw new RuntimeException(e);
    }
    LOG.debug("No Mule Module System descriptor found for the archive {}.", archive.getName());
  }

  // TODO: Validate that the element we receive comes from the archive used to create this instance.
  @Override
  public boolean isApi(JavaTypeElement element) {
    String packageName = getPackageName(element);
    return standardPackages.contains(packageName) || privilegedPackages.contains(packageName);
  }

  @Override
  public boolean isEmpty() {
    return privilegedPackages.isEmpty() && standardPackages.isEmpty();
  }

  @Override
  public String toString() {
    return format("Mule Runtime Module System API. Module: %s. Standard packages: %s. Privileged packages: %s", moduleName,
                  standardPackages, privilegedPackages);
  }

  private void loadExportedPackages(Properties muleModuleSystemProperties) {
    standardPackages.addAll(getPackagesFromProperty(muleModuleSystemProperties, EXPORTED_CLASS_PACKAGES_PROPERTY));
    privilegedPackages.addAll(getPackagesFromProperty(muleModuleSystemProperties, PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY));
  }

  private Properties getProperties(JarInputStream propertiesFile) throws IOException {
    Properties properties = new Properties();
    byte[] bytes = getBytes(new BufferedInputStream(propertiesFile));
    properties.load(new ByteArrayInputStream(bytes));
    return properties;
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

  private Set<String> getPackagesFromProperty(Properties properties, String propertyName) {
    Set<String> result = new HashSet<>();
    String property = properties.getProperty(propertyName);
    if (property != null) {
      String[] packages = property.split(",");
      for (String packageName : packages) {
        String name = packageName.trim();
        if (!name.isEmpty()) {
          result.add(name);
        }
      }
    }
    return result;
  }

}
