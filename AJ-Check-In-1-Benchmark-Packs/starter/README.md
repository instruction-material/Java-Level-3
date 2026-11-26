# Compare linear and binary search

Implement measure for a target array and an IntPredicate that reports membership. Start the clock immediately before visiting the queries. Call the predicate once per target in array order. Count positive results, then return the elapsed nanoseconds and count. Empty targets produce zero matches.

Both algorithms receive the same sorted array and the same 2,000 queries. The fixture provides 1,000 guaranteed hits and 1,000 guaranteed misses. Generation, sorting and reports happen outside the measured loop. Warm up both methods, alternate run order and report the mean over four trials. Compare search time on this workload. Sorting cost, hardware and Java warmup matter when generalizing.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
