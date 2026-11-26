# Build an itinerary mileage report

An itinerary carries a starting stop and an ordered list of connected directed road legs with whole-mile costs. Return each reached stop with cumulative mileage, including the starting stop at zero. Reject broken links, negative distance and totals outside long.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
