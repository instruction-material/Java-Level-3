# Hash table resizing extension practice

Redistribute existing chained entries into a new bucket count. Recompute every destination using the new modulus, keep every key and value exactly once, and leave the old buckets intact. Reject nonpositive target capacities.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
