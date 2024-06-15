// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver.exception;

/**
 * This is thrown if a circular dependency is detected.
 * 
 * @author Rob
 *
 */
@SuppressWarnings("serial")
public class CircularDependencyException extends DependencyException {

  /**
   * @param dependency
   */
  public CircularDependencyException(String dependency) {
    super("Circular dependency for " + dependency);
  }

}
