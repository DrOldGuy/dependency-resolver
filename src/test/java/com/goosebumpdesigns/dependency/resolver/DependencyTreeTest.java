// Copyright (c) 2020 Goosebump Designs LLC

package com.goosebumpdesigns.dependency.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * @author Rob
 *
 */
class DependencyTreeTest {

  /**
   * 
   */
  @Test
  void testWithStrings() {
    // Given: a graph of dependencies
    DependencyGraph<String> graph = new DependencyGraph<>();
    List<String> expected = Arrays.asList("a", "b", "c", "d", "e");

    graph.add("e").dependsOn("d");
    graph.add("d").dependsOn("a").dependsOn("b");
    graph.add("c");
    graph.add("b").dependsOn("a");
    graph.add("a");

    // When: the dependencies are sorted
    List<String> actual = graph.sort();

    // Then: the list is sorted in dependency order
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * 
   */
  @Test()
  void testWithCircularDependency() {
    // Given: a graph of dependencies
    DependencyGraph<String> graph = new DependencyGraph<>();

    graph.add("a").dependsOn("c");
    graph.add("b");
    graph.add("c").dependsOn("e");
    graph.add("d");
    graph.add("e").dependsOn("a");

    // When: the dependencies are sorted
    assertThatThrownBy(() -> {
      graph.sort();
    }).isInstanceOf(CircularDependencyException.class);

    // Then: an exception is thrown
  }

  /**
   * 
   */
  @Test
  void testWithInvalidDependency() {
    // Given: a graph of dependencies
    DependencyGraph<String> graph = new DependencyGraph<>();

    graph.add("a").dependsOn("c");
    graph.add("b");
    graph.add("c").dependsOn("e");
    graph.add("d");
    graph.add("e").dependsOn("f");

    // When: the dependencies are sorted
    assertThatThrownBy(() -> {
      graph.sort();
    }).isInstanceOf(MissingDependencyException.class);

    // Then: an exception is thrown
  }

  /**
   * 
   */
  @Test
  void testWithSelfDependency() {
    // Given: a graph of dependencies
    DependencyGraph<String> graph = new DependencyGraph<>();

    graph.add("a").dependsOn("c");
    graph.add("b");
    graph.add("c").dependsOn("e");
    graph.add("d");
    graph.add("e").dependsOn("e");

    List<String> expected = Arrays.asList("e", "b", "c", "d", "a");

    // When: the dependencies are sorted
    List<String> actual = graph.sort();

    // Then: an exception is thrown
    assertThat(actual).isEqualTo(expected);
  }

  /**
   * 
   */
  @Test
  void testWithClass() {
    // Given: a graph of dependencies
    DependencyGraph<Element> graph = new DependencyGraph<>();

    Element a = new Element("a");
    Element b = new Element("b");
    Element c = new Element("c");
    Element d = new Element("d");
    Element e = new Element("e");

    graph.add(e).dependsOn(d);
    graph.add(d).dependsOn(a).dependsOn(b);
    graph.add(c);
    graph.add(b).dependsOn(a);
    graph.add(a);

    List<Element> expected = Arrays.asList(a, b, c, d, e);

    // When: the dependencies are sorted
    List<Element> actual = graph.sort();

    // Then: the list is sorted in dependency order
    assertThat(actual).isEqualTo(expected);
  }

  static class Element {
    String name;

    public Element(String name) {
      this.name = name;
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Element other = (Element) obj;
      return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
      return "Element [name=" + name + "]";
    }
  }
}
