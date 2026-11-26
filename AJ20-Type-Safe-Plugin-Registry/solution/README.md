# Select a command through a generic interface

Store named Plugin<T> objects and call them with input of type T. Normalize names by removing surrounding space. Reject blank names, duplicate names and unknown commands. Demonstrate four string plugins and one integer plugin through the same registry class.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
