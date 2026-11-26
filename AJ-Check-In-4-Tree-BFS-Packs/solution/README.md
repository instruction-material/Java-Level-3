# Compare breadth-first and depth-first tree order

Implement level-order traversal. A queue visits the root, then all its children, then their children. At each node enqueue left before right. The supplied preorder visits each left subtree first. Return a list and leave the tree unchanged.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
