/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuleModuleSystemExportedPackages implements ExportedPackages {

  private static final String MULE_MODULE_NAME_PROPERTY = "module.name";
  private static final String EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.export.classPackages";
  private static final String PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY = "artifact.privileged.classPackages";

  private static final Logger LOG = LoggerFactory.getLogger(MuleModuleSystemExportedPackages.class);

  private final Set<String> standardPackages = new HashSet<>();
  private final Set<String> privilegedPackages = new HashSet<>();
  private final String moduleName;

  public MuleModuleSystemExportedPackages(Properties muleModuleSystemProperties) {
    this.moduleName = muleModuleSystemProperties.getProperty(MULE_MODULE_NAME_PROPERTY);
    Set<String> standardPackages = getPackagesFromProperty(muleModuleSystemProperties, EXPORTED_CLASS_PACKAGES_PROPERTY);
    Set<String> privilegedPackages =
        getPackagesFromProperty(muleModuleSystemProperties, PRIVILEGED_EXPORTED_CLASS_PACKAGES_PROPERTY);
    this.standardPackages.addAll(standardPackages);
    this.privilegedPackages.addAll(privilegedPackages);
  }


  @Override
  public boolean isExported(String packageName) {
    return standardPackages.contains(packageName) || privilegedPackages.contains(packageName);
  }

  @Override
  public void logExportedPackages() {
    LOG.info("Adding exported packages from Mule Module: {}\nstandard: {}\nprivileged: {}", moduleName, standardPackages,
             privilegedPackages);
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
