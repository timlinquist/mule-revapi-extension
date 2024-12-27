/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import java.lang.module.ModuleDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaModuleSystemExportedPackages implements ExportedPackages {

  private static final Logger LOG = LoggerFactory.getLogger(JavaModuleSystemExportedPackages.class);

  private final ModuleDescriptor descriptor;

  public JavaModuleSystemExportedPackages(ModuleDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  @Override
  public boolean isExported(String packageName) {
    return descriptor.isAutomatic() || descriptor.exports().stream().anyMatch(exports -> exports.source().equals(packageName));
  }

  @Override
  public void logExportedPackages() {
    LOG.info("Adding exported packages from Java Module: {}\nexports: {}", descriptor.name(),
             descriptor.isAutomatic() ? "ALL (automatic module)" : descriptor.exports());
  }
}
