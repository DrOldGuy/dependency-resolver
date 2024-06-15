# Dependency Resolver

This package resolves dependencies of any type and returns a sorted list from least dependent to most dependent. The calling application is responsible for determining the dependencies.

## Why would I use this?

This code was originally developed to auto-generate SQL CREATE TABLE statements (DDL). Dependencies are identified by foreign key relationships in the CREATE TABLE statements. When the tables are added to the dependency graph and the dependencies identified, the tables are then sorted in the correct order for the CREATE TABLE statements.

Dependencies are identified by the calling application. The dependency graph is responsible to sort the dependencies from least-dependent to most-dependent order.

## Usage

The application defines what a dependency is and the resolver returns a sorted list of dependencies. To use, write code something like this:

```
 1 Thing a = new Thing();
 2 Thing b = new Thing();
 3 Thing c = new Thing();
 4 Thing d = new Thing();
 5 Thing e = new Thing();
 6 
 7 DependencyGraph<Thing> graph = new DependencyGraph<>();
 8
 9 graph.add(e).dependsOn(d);
10 graph.add(d).dependsOn(a).dependsOn(b);
11 graph.add(c);
12 graph.add(b).dependsOn(a);
13 graph.add(a);
14
15 List<Thing> sorted = graph.sort();
16
17 // The returned list is: [a, b, c, d, e], or [a, c, b, d, e]
```

Note: line 10 says that d depends on a and b, not that d depends on a and a depends on b.

You can also do this if you don't know the dependencies in advance:

```
 1 Thing a = new Thing();
 2 Thing b = new Thing();
 3 Thing c = new Thing();
 4 Thing d = new Thing();
 5 Thing e = new Thing();
 6 
 7 DependencyGraph<Thing> graph = new DependencyGraph<>();
 8
 9 GraphNode<Thing> nodeA = graph.add(a);
10 GraphNode<Thing> nodeB = graph.add(b);
11 GraphNode<Thing> nodeC = graph.add(c);
12 GraphNode<Thing> nodeD = graph.add(d);
13 GraphNode<Thing> nodeE = graph.add(e);
14
15 while(some process runs) {
16   nodeC.dependsOn(e);
17   nodeA.dependsOn(b);
18   ...
19 }
20
21 List<Thing> sorted = graph.sort();
```

## How it works

This uses a brute force algorithm to figure out the dependencies. There may be more elegant ways of doing the sort but I couldn't find anything when I wrote this.

## Exceptions that may be thrown

**MissingDependencyException** This is thrown if a dependency is encountered but the dependency is not known in advance. For example:

```
 1 Thing a = new Thing();
 2 Thing b = new Thing();
 3
 4 DependencyGraph<Thing> graph = new DependencyGraph<>();
 5
 6 graph.add(a).dependsOn(b);
 7 // Missing: graph.add(b)
 8
 9 List<Thing> sorted = graph.sort();
```

Line 6 throws a MissingDependencyException because the object *b* was never added to the graph. Note that you don't need to have added *b* prior to indicating the dependent relationship in line 6. You only need to have added *b* prior to sorting the dependencies.

**CircularDependencyException** This is thrown if the sort method detects a circular dependency like this:

```
 1 Thing a = new Thing();
 2 Thing b = new Thing();
 3 Thing c = new Thing();
 4
 5 DependencyGraph<Thing> graph = new DependencyGraph<>();
 6
 7 graph.add(a).dependsOn(b);
 8 graph.add(b).dependsOn(c);
 9 graph.add(c).dependsOn(a); // Circular dependency: a -> b -> c -> a
10
11 List<Thing> sorted = graph.sort();
```

In the code above, line 11 causes a CircularDependencyException to be thrown.

**DependencyException** This is the base class of the other exceptions. It can be used to catch either type of exception.