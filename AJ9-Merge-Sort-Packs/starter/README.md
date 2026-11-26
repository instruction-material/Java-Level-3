# Merge sort

Return a separate ascending array for any int array, including empty or singleton input. Recursively copy and sort both halves, then join them using the supplied cursor merge. Use a new array in the base case so each result can be changed without altering its input.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
