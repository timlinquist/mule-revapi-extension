/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.mule.tools.revapi.transform;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.Types;

import org.revapi.AnalysisContext;
import org.revapi.Difference;
import org.revapi.DifferenceTransform;
import org.revapi.Element;

/**
 * Base class for implementing annotation based difference transformations.
 *
 * @since 1.1
 */
public abstract class AbstractApiAnnotationTransform implements DifferenceTransform {

  private final String id;
  private final Map<String, DifferenceChecker> checkers;

  /**
   * Creates a new transformer
   *
   * @param id identifies the extension inside Revapi framework
   */
  AbstractApiAnnotationTransform(String id) {
    this.id = id;
    checkers = getDifferenceCheckers();
  }

  @Override
  public Difference transform(Element oldElement, Element newElement, Difference difference) {
    DifferenceChecker differenceChecker = checkers.get(difference.code);
    if (differenceChecker != null && differenceChecker.ignore(oldElement, newElement)) {
      return null;
    } else {
      return difference;
    }
  }

  @Override
  public void close() throws Exception {

  }

  @Override
  public Reader getJSONSchema() {
    return null;
  }

  @Override
  public void initialize(AnalysisContext analysisContext) {

  }

  @Override
  public String getExtensionId() {
    return id;
  }

  @Override
  public Pattern[] getDifferenceCodePatterns() {
    List<Pattern> patterns = new LinkedList<>();
    for (String code : getDifferenceCodes()) {
      patterns.add(getPatternFor(code));
    }

    return patterns.toArray(new Pattern[0]);
  }

  /**
   * @return Revapi difference codes that the extension will process.
   */
  protected abstract String[] getDifferenceCodes();

  /**
   * @return difference checkers to use for each difference code.
   */
  protected abstract Map<String, DifferenceChecker> getDifferenceCheckers();

  private Pattern getPatternFor(String code) {
    return Pattern.compile("^" + Pattern.quote(code) + "$");
  }

  boolean isTypeWithAnnotation(Types types, TypeMirror type, SimpleTypeVisitor8<Boolean, Void> checker) {

    if (type.accept(checker, null)) {
      return true;
    }

    List<? extends TypeMirror> superTypes = types.directSupertypes(type);

    for (TypeMirror t : superTypes) {
      if (t.accept(checker, null)) {
        return true;
      }
      if (isTypeWithAnnotation(types, t, checker)) {
        return true;
      }
    }

    return false;
  }
}
