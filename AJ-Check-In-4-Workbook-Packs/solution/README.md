# Verify tree, hash and graph invariants

Check that a tree respects all ancestor bounds, trace a bounded hash probe across tombstones, and validate a proposed directed route by checking its vertices, edges and long cost. The optional shortest-hops mode uses BFS on the same weighted matrix and counts edges instead of cost.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional BFS comparison, run `java ContractTests hops` and `java Main hops`.
