// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import com.goosebumpdesigns.dependency.resolver.exception.CircularDependencyException;
import com.goosebumpdesigns.dependency.resolver.exception.MissingDependencyException;

/**
 * This class accepts elements and element dependencies. The {@link #sort()} method sorts the
 * elements in dependency order. Use the class like this:
 * 
 * <pre>
 * graph.add("e").dependsOn("d");
 * graph.add("d").dependsOn("a").dependsOn("b");
 * graph.add("c");
 * graph.add("b").dependsOn("a");
 * graph.add("a");
 * </pre>
 * 
 * This depends on knowing semantic equality and not physical equality. It will give unpredictable
 * results if the element being sorted does not have a .equals() method.
 * 
 * @author Rob
 *
 */
public class DependencyGraph<T> {
  private List<GraphNode<T>> elements = new LinkedList<>();

  /**
   * @param string
   */
  public GraphNode<T> add(T element) {
    GraphNode<T> node = new GraphNode<>(element);
    elements.add(node);

    return node;
  }

  /**
   * This method sorts the dependencies and returns a list in sorted order. Items at the end of the
   * list may be dependent on items at the start of the list. So, for the elements and dependencies:
   * 
   * <pre>
   * e -> d
   * d -> a, b
   * c
   * b -> a
   * a
   * </pre>
   * 
   * This will return [a, b, c, d, e], or [a, c, b, d, e]
   * 
   * @throws CircularDependencyException This is thrown if a circular dependency is detected:
   * 
   *         <pre>
   * a -> b
   * b -> c
   * c -> a
   *         </pre>
   * 
   * @throws MissingDependencyException This is thrown if a dependency can't be found in the list of
   *         elements:
   * 
   *         <pre>
   * a -> b
   * b -> c
   * c -> d
   *         </pre>
   * 
   * @return
   */
  @SuppressWarnings("java:S3776")
  public List<T> sort() {
    GraphNode<T> start = null;
    List<GraphNode<T>> bucket = new LinkedList<>(elements);
    List<T> resolved = new LinkedList<>();

    while(!bucket.isEmpty()) {
      GraphNode<T> test = bucket.remove(0);

      if(Objects.isNull(start)) {
        start = test;
      }
      else if(start.equals(test)) {
        throw new CircularDependencyException(test.toString());
      }

      if(test.getDependencies().isEmpty() || allDependenciesResolved(resolved, test)) {
        resolved.add(test.getElement());
        start = null;
      }
      else {
        boolean found = false;

        for(int index = 0; !found && index < bucket.size(); index++) {
          GraphNode<T> node = bucket.get(index);

          if(test.getDependencies().contains(node.getElement())) {
            bucket.set(index, test);
            bucket.add(0, node);
            found = true;
          }
        }

        if(!found) {
          throw new MissingDependencyException(test.toString());
        }
      }
    }

    return resolved;
  }

  /**
   * This will return true if all the dependencies in the test node are already resolved or is the
   * node under test. This allows a node to have a dependency on itself.
   * 
   * @param resolved
   * @param test
   * @return
   */
  private boolean allDependenciesResolved(List<T> resolved, GraphNode<T> test) {
    List<T> testList = new LinkedList<>(resolved);

    if(testList.containsAll(test.getDependencies())) {
      return true;
    }

    testList.add(test.getElement());

    return testList.containsAll(test.getDependencies());
  }

}
