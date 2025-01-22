/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import static java.lang.String.format;

import org.revapi.java.spi.JavaModelElement;
import org.revapi.java.spi.JavaTypeElement;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.lang.model.element.ModuleElement;

/**
 * An {@link ApiBoundary} that defines a JPMS API boundary. The boundary is defined by reading the module exported packages. A
 * list of regular expressions can be provided in order to leave out exported packages that should not be part of the API boundary
 * because they are directed to modules where the cost of refactoring can be assumed (non-directed exports cannot be excluded).
 */
public class JavaModuleSystemApiBoundary implements ApiBoundary {

  private final ModuleElement moduleElement;
  private final List<String> exportedPackages;

  public JavaModuleSystemApiBoundary(JavaModelElement element, List<String> excludedTargets) {
    this.moduleElement = element.getTypeEnvironment().getElementUtils().getModuleOf(element.getDeclaringElement());
    this.exportedPackages =
        moduleElement.getDirectives().stream().map(directive -> directive.accept(new IsApiDirective(excludedTargets), null))
            .filter(Objects::nonNull)
            .toList();
  }

  @Override
  public boolean isApi(JavaTypeElement element) {
    return exportedPackages.contains(getPackageName(element));
  }

  @Override
  public boolean isEmpty() {
    return exportedPackages.isEmpty();
  }

  /**
   * @return True if this API is open (therefore all its elements are API).
   */
  public boolean isOpen() {
    return moduleElement.isOpen();
  }

  @Override
  public String toString() {
    return format("Java Platform Module System API. Module: %s., Packages: %s", moduleElement.getQualifiedName(),
                  exportedPackages);
  }

  /**
   * A jpms module directive visitor that can return the packages that are part of a jpms module API boundary.
   */
  private static class IsApiDirective implements ModuleElement.DirectiveVisitor<String, Void> {

    private final List<Pattern> excludedTargets;

    /**
     * Constructor.
     * 
     * @param excludedTargets List of regular expressions that will exclude any export that only targets matching modules.
     */
    public IsApiDirective(List<String> excludedTargets) {
      this.excludedTargets = excludedTargets.stream().map(Pattern::compile).toList();
    }

    @Override
    public String visitRequires(ModuleElement.RequiresDirective d, Void unused) {
      // Nothing to do.
      return null;
    }

    /**
     * Visits a jpms export directive in order to determine if the package should be part of the jpms module API or not.
     * 
     * @param d      The visited jpms export directive (visitor pattern).
     * @param unused This parameter is not used (visitor pattern).
     * @return The exported package if it should be part of the jpms module API, null otherwise.
     */
    @Override
    public String visitExports(ModuleElement.ExportsDirective d, Void unused) {
      boolean isApi = d.getTargetModules() == null
          || d.getTargetModules().stream()
              .anyMatch(moduleElement -> excludedTargets.stream()
                  .noneMatch(pattern -> pattern.matcher(moduleElement.getQualifiedName()).matches()));
      if (isApi) {
        return d.getPackage().getQualifiedName().toString();
      } else {
        return null;
      }
    }

    @Override
    public String visitOpens(ModuleElement.OpensDirective d, Void unused) {
      // Nothing to do.
      return null;
    }

    @Override
    public String visitUses(ModuleElement.UsesDirective d, Void unused) {
      // Nothing to do.
      return null;
    }

    @Override
    public String visitProvides(ModuleElement.ProvidesDirective d, Void unused) {
      // Nothing to do.
      return null;
    }
  }
}
