/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.revapi;

import org.revapi.java.model.TypeElement;
import org.revapi.java.spi.JavaModelElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.ModuleElement;
import java.util.List;
import java.util.Objects;

public class JavaModuleSystemApiBoundary implements ApiBoundary {

  private static final Logger LOG = LoggerFactory.getLogger(JavaModuleSystemApiBoundary.class);

  private final ModuleElement moduleElement;
  private final List<String> exportedPackages;

  public JavaModuleSystemApiBoundary(JavaModelElement element) {
    IsApiDirective isApiDirective = new IsApiDirective();
    this.moduleElement = element.getTypeEnvironment().getElementUtils().getModuleOf(element.getDeclaringElement());
    // TODO: Consider flatMap (for the nulls)
    this.exportedPackages =
        moduleElement.getDirectives().stream().map(directive -> directive.accept(isApiDirective, null)).filter(Objects::nonNull)
            .toList();
  }

  @Override
  public boolean isApi(TypeElement element) {
    return exportedPackages.contains(getPackageName(element));
  }

  @Override
  public boolean isEmpty() {
    return exportedPackages.isEmpty();
  }

  public boolean isOpen() {
    return moduleElement.isOpen();
  }

  // TODO: This will be refactored to a toString at the implementations.
  @Override
  public void logApiPackages() {
    // TODO: IMPLEMENT.
  }

  private static class IsApiDirective implements ModuleElement.DirectiveVisitor<String, Void> {

    @Override
    public String visitRequires(ModuleElement.RequiresDirective d, Void unused) {
      return null;
    }

    @Override
    public String visitExports(ModuleElement.ExportsDirective d, Void unused) {
      return d.getPackage().getQualifiedName().toString();
    }

    @Override
    public String visitOpens(ModuleElement.OpensDirective d, Void unused) {
      return null;
    }

    @Override
    public String visitUses(ModuleElement.UsesDirective d, Void unused) {
      return null;
    }

    @Override
    public String visitProvides(ModuleElement.ProvidesDirective d, Void unused) {
      return null;
    }
  }
}
