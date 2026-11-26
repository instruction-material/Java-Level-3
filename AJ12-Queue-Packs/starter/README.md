# Implementing a circular queue

Complete a fixed-capacity integer FIFO queue. Maintain the front index and count. Use the logical rear offset to find the next insertion slot with wraparound. An empty peek or dequeue throws NoSuchElementException. A full enqueue throws IllegalStateException and leaves the contents intact.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
