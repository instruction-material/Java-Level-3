# Graph traversal transfer practice

Produce breadth-first visit order in a directed graph. Enqueue neighbors in list order and mark them when enqueued. A missing start returns an empty list. A destination with no existing row has no outgoing neighbors. Keep the original adjacency map intact.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
