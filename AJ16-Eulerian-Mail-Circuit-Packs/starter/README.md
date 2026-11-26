# Classify an Eulerian mail route

Classify an undirected street graph as a circuit, an open trail, or neither. Every street must be used once, parallel streets count independently, and a loop adds two to its vertex degree. Ignore isolated vertices for street connectivity. A graph with declared vertices and no streets admits a zero-length circuit. An empty vertex set has no start.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
