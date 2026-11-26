# Remove and clear tree keys

Return the replacement subtree when removing a key. Retain the original subtree on a miss. For a node with two children use the largest key in the left subtree, remove that predecessor from its old position and attach both subtrees. The tree owns its nodes and clears by resetting root and size. The public removal counts one change only for a present key.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
