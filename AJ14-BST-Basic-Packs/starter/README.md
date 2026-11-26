# Implement a binary search tree

Insert distinct integers and return the resulting root. The returned root can differ when the original tree is empty. If a key already exists, leave the tree and its node count unchanged. Find a key by following ordered left or right links. For the optional ceiling task, return the smallest key at least as large as the query, or OptionalInt.empty() if none exists.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional ceiling search, run `java ContractTests ceiling` and `java Main ceiling`.
