# Trace recursive depth-first traversal

Use the same tree for preorder, inorder and postorder. Append a key once when its visit position is reached. Reuse one accumulator per call. The fixture builder and tree are supplied. Return an empty list for a null root. Preserve the tree across all traversals.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
