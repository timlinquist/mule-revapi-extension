/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaModuleSystemExportedPackages implements ExportedPackages {

  private static final Logger LOG = LoggerFactory.getLogger(JavaModuleSystemExportedPackages.class);

  private final Module module;

  public JavaModuleSystemExportedPackages(Module module) {
    this.module = module;
  }

  @Override
  public boolean isExported(String packageName) {
    return module.getDescriptor().isAutomatic()
        || module.getDescriptor().exports().stream().anyMatch(exports -> exports.source().equals(packageName));
  }

  @Override
  public void logExportedPackages() {
    LOG.info("Adding exported packages from Java Module: {}\nexports: {}", module.getDescriptor().name(),
             module.getDescriptor().isAutomatic() ? "ALL (automatic module)" : module.getDescriptor().exports());
  }
}
