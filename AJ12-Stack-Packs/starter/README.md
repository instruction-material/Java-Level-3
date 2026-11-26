# Implementing a stack

Complete the fixed-capacity integer stack. The field size is both the count and the next free index. Push, pop and peek take constant time. Throw IllegalStateException on a full push and NoSuchElementException on an empty peek or pop. Reject nonpositive capacities at construction.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
