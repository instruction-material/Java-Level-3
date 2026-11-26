# Compare breadth-first and depth-first tree order

Implement level-order traversal. A queue visits the root, then all its children, then their children. At each node enqueue left before right. The supplied preorder visits each left subtree first. Return a list and leave the tree unchanged.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
