# BST mutation extension practice

Trim a tree to keep only keys in an inclusive interval. Reuse the surviving nodes and reconnect the surviving child roots. Return the new root, which can differ from the original. A reversed interval discards the entire tree.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
