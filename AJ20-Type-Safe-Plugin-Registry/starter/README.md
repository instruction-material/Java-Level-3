# Select a command through a generic interface

Store named Plugin<T> objects and call them with input of type T. Normalize names by removing surrounding space. Reject blank names, duplicate names and unknown commands. Demonstrate four string plugins and one integer plugin through the same registry class.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
