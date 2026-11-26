# Merge sort

Return a separate ascending array for any int array, including empty or singleton input. Recursively copy and sort both halves, then join them using the supplied cursor merge. Use a new array in the base case so each result can be changed without altering its input.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
