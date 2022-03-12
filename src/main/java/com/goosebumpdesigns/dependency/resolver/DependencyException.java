// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver;

/**
 * This is the base class for both {@link CircularDependencyException} and {@link MissingDependencyException} so that
 * they can both be caught by catching this exception.
 *
 */
@SuppressWarnings("serial")
public class DependencyException extends RuntimeException {

  /**
   * @param message
   */
  public DependencyException(String message) {
    super(message);
  }
}
