# Recursive binary search

Given a sorted int array, search returns one matching index or -1. Use a recursive method with inclusive bounds. The empty interval is the first base case. The matching midpoint is the second. Forward the smaller range with the midpoint excluded. For [2, 4, 10, 27, 38] and target 38, the active intervals are [0, 4], [3, 4], and [4, 4].

This role includes unfinished methods that identify themselves when called.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
