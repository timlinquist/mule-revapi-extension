/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.ModuleLayer.boot;
import static java.lang.ModuleLayer.defineModulesWithOneLoader;
import static java.lang.module.ModuleFinder.ofSystem;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static java.util.stream.Stream.concat;
import static java.util.stream.StreamSupport.stream;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.revapi.API;
import org.revapi.Archive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaModuleSystemExportedPackages implements ExportedPackages {

  private static final Logger LOG = LoggerFactory.getLogger(JavaModuleSystemExportedPackages.class);

  private final Module module;

  public JavaModuleSystemExportedPackages(ModuleReference moduleReference, API api) {
    this.module = loadJpmsModule(moduleReference, api);
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
    return module.getDescriptor().isAutomatic()
        || module.getDescriptor().exports().stream().anyMatch(exports -> exports.source().equals(packageName));
  }

  @Override
  public void logExportedPackages() {
    LOG.info("Adding exported packages from Java Module: {}\nexports: {}", module.getDescriptor().name(),
             module.getDescriptor().isAutomatic() ? "ALL (automatic module)" : module.getDescriptor().exports());
  }

  private Module loadJpmsModule(ModuleReference moduleReference, API api) {
    // TODO: Introduce module layer cache.
    final Path[] apiModulePath = concat(stream(api.getArchives().spliterator(), true),
                                        api.getSupplementaryArchives() != null
                                            ? stream(api.getSupplementaryArchives().spliterator(), true)
                                            : Stream.empty()).map(JavaModuleSystemExportedPackages::getPath).toArray(Path[]::new);
    final ModuleFinder finder = ModuleFinder.of(apiModulePath);
    final Configuration configuration =
        boot().configuration().resolve(finder, ofSystem(), singleton(moduleReference.descriptor().name()));
    ModuleLayer.Controller controller = defineModulesWithOneLoader(configuration,
                                                                   singletonList(boot()),
                                                                   getSystemClassLoader());
    return controller.layer().findModule(moduleReference.descriptor().name())
        .orElseThrow(() -> new RuntimeException("Could not find jpms module: " + moduleReference.descriptor().name() + " at API: "
            + api));
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
