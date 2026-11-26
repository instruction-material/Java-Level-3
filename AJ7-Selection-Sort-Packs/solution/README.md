# Selection sort

Sort the supplied int array in place. For each position, select the index of a minimum in the remaining suffix and exchange it with the current position. Keep the first minimum while scanning. Return the count of candidate-value comparisons, including those that leave the candidate unchanged. For [4, 1, 3, 1], the sorted array is [1, 1, 3, 4] after six comparisons.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
