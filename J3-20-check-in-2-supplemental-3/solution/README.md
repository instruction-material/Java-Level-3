# Select only the smallest k items

Return the smallest k items in ascending order, retaining multiplicity. Keep the input unchanged. Make a working copy and finish only the first k selection passes. Reject k below zero or above the input length.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
