# Compare four sorting algorithms

Implement timed. Make a working copy before starting the clock, time only the sort, and verify the returned array list against an independently sorted reference after stopping the clock. Return elapsed nanoseconds. The driver compares the same seeded data, inputs in three orders, a warmup, and four trials with rotating execution order.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```
