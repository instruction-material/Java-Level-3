# Implementing a circular deque

Complete a fixed-capacity integer deque with add, peek and remove at each end. Track the front index and size. A full add throws IllegalStateException. Empty peek and removal operations throw NoSuchElementException. Reject a nonpositive capacity before allocating storage.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
