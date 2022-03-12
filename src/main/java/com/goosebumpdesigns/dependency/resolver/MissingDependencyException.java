// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver;

/**
 * This is thrown if a dependency is not in the list of known elements when the dependency graph is sorted.
 * 
 * @author Rob
 *
 */
@SuppressWarnings("serial")
public class MissingDependencyException extends DependencyException {

  /**
   * @param dependency
   */
  public MissingDependencyException(String dependency) {
    super("Missing dependency for " + dependency);
  }

}
