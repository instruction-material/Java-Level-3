# Identify shifts before insertion sort

An inversion is an earlier larger value followed by a smaller one. Count all inversions without modifying the input. Each inversion corresponds to one shift in the insertion-sort implementation. The scan for pairs takes quadratic time even when the input is already sorted.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
