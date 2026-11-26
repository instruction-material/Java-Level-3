# Sort in both directions

Implement a bidirectional adjacent sorter in place. After a forward pass, shrink the upper boundary. After a backward pass, raise the lower boundary. If a pass performs no swaps, the remaining active range is sorted.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
