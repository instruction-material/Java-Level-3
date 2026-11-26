# Build a directed adjacency list

Store a directed graph in a map of outgoing-neighbor sets. Retain isolated vertices. Add both endpoints of every edge, store each ordered pair once, and count a self-edge once. Null vertex names are invalid. Report independent read-only lists in insertion order.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
