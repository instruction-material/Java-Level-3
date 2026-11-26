# Find a shortest weighted route

Use a square directed adjacency matrix. A cell of minus one means no edge. Zero and positive weights are valid, including zero-cost cycles. Return an optional Route containing the total long distance and a source-to-target list. Reject invalid indices, ragged matrices and weights below minus one. Queue entries are immutable snapshots, and a stale entry must be skipped.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
