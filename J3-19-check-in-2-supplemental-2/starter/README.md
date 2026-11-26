# Verify sorting and element counts

Check that a candidate is nondecreasing and contains each value exactly as often as the input. Return false for a length mismatch, reversed adjacent pair, missing value or extra value. Use a count map so repeated values and negatives are handled.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
