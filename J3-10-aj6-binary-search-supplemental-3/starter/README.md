# Binary search edge-case practice

Return the first index whose value is greater than or equal to the target in a sorted array. Return length if all values are smaller. This insertion position is zero for an empty array. Keep an interval of candidate positions. With duplicates, a match can leave earlier valid positions, so shrink the upper bound to the midpoint. For [2, 4, 4, 4, 9], targets 4, 5 and 10 return 1, 4 and 5.

This role includes unfinished methods that identify themselves when called.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
