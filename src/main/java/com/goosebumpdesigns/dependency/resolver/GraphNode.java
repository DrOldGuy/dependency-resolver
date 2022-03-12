// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rob
 *
 */
public final class GraphNode<T> {
  private T element;
  private List<T> dependencies = new LinkedList<>();

  /**
   * 
   * @param element
   */
  public GraphNode(T element) {
    this.element = Objects.requireNonNull(element);
  }

  /**
   * Use this method to add a dependency. A dependency does not have to exist in the graph when it is added but it must
   * exist when the graph is sorted. This method returns the Node object so that it can be called more than once for
   * multiple dependencies as in:
   * 
   * <pre>
   * graph.add("a").dependsOn("b").dependsOn("c");
   * graph.add("b").dependsOn("c");
   * graph.add("c");
   * </pre>
   * 
   * @param dependency
   * @return
   */
  public GraphNode<T> dependsOn(T dependency) {
    dependencies.add(dependency);
    return this;
  }

  public T getElement() {
    return element;
  }

  public List<T> getDependencies() {
    return dependencies;
  }

  @Override
  public int hashCode() {
    return Objects.hash(element);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GraphNode<?> other = (GraphNode<?>) obj;
    return Objects.equals(element, other.element);
  }

  @Override
  public String toString() {
    return "GraphNode [element=" + element + ", dependencies=" + dependencies + "]";
  }
}
