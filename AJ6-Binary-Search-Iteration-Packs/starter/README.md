# Iterative binary search

Given a sorted int array, search returns one matching index or -1. Bounds are inclusive. An empty array has no valid index. Duplicates can yield any matching index. The optional measure method returns the index and counts one probe per midpoint, including a match. For [2, 4, 10, 27, 38], searching for 2 visits indices 2 then 0. An empty array takes zero probes.

This role includes unfinished methods that identify themselves when called.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the comparison-count extension, also run `java ContractTests count` and `java Main count`.
