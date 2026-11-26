# Verify sorting and element counts

Check that a candidate is nondecreasing and contains each value exactly as often as the input. Return false for a length mismatch, reversed adjacent pair, missing value or extra value. Use a count map so repeated values and negatives are handled.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
