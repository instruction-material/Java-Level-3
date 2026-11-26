# Bubble sort and early stopping

Sort in place by comparing adjacent values, exchanging those out of order. Return a long comparison count. On each pass the largest unsorted value moves to the end of the active range. When earlyStop is true, stop after a pass with no swaps. The optional last-exchange extension also shrinks the next pass to the final position where a swap occurred.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the last-exchange challenge, use `java ContractTests last` and `java Main last`.
