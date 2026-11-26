# An array of typed nodes

Implement Node<T> with typed reads and updates. An individual node retains its value type. Use Node<?>[] for an array with differently typed nodes. A wildcard node can be read as Object. Callers with a concrete Node<String> or Node<Integer> keep typed get and set operations. Support null values by formatting them with String.valueOf.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
