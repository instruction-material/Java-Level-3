# Build an itinerary mileage report

An itinerary carries a starting stop and an ordered list of connected directed road legs with whole-mile costs. Return each reached stop with cumulative mileage, including the starting stop at zero. Reject broken links, negative distance and totals outside long.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
