# Traverse a two-dimensional array

Build a grid containing row index plus column index, then find the largest value in a possibly ragged grid. Return OptionalInt.empty for a grid with no values. Negative values are legal. Search for a target by row, and create a seeded rectangular grid of doubles in [0, 10).

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
