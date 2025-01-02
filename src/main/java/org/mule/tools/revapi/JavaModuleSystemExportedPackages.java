/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.util.Optional.empty;

import java.io.File;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.revapi.Archive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaModuleSystemExportedPackages implements ExportedPackages {

  private static final Logger LOG = LoggerFactory.getLogger(JavaModuleSystemExportedPackages.class);

  private final ModuleReference moduleReference;

  public JavaModuleSystemExportedPackages(ModuleReference moduleReference) {
    this.moduleReference = moduleReference;
  }

  public static Optional<ModuleReference> findJpmsModuleReference(Archive archive) {
    try {
      ModuleFinder moduleFinder = ModuleFinder.of(getPath(archive));
      Set<ModuleReference> moduleReferences = moduleFinder.findAll();
      if (moduleReferences.size() > 1) {
        throw new IllegalArgumentException("More than one jpms module found for the archive " + archive.getName() + ": "
            + getModuleNames(moduleReferences));
      }
      if (moduleReferences.isEmpty()) {
        return empty();
      } else {
        return Optional.of(moduleReferences.iterator().next());
      }
    } catch (Exception e) {
      LOG.error("Error while finding modules for archive: {}", archive.getName(), e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean isExported(String packageName) {
    return moduleReference.descriptor().isAutomatic()
        || moduleReference.descriptor().exports().stream().anyMatch(exports -> exports.source().equals(packageName));
  }

  @Override
  public void logExportedPackages() {
    LOG.info("Adding exported packages from Java Module: {}\nexports: {}", moduleReference.descriptor().name(),
             moduleReference.descriptor().isAutomatic() ? "ALL (automatic module)" : moduleReference.descriptor().exports());
  }

  private static Path getPath(Archive archive) {
    try {
      Field archiveFile = archive.getClass().getDeclaredField("file");
      archiveFile.setAccessible(true);
      return ((File) archiveFile.get(archive)).toPath();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String getModuleNames(Set<ModuleReference> moduleReferences) {
    return moduleReferences.stream().map(moduleReference -> moduleReference.descriptor().name())
        .collect(Collectors.joining(", "));
  }
}
