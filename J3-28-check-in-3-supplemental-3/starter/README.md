# Undo history with a stack and map

Read a sequence of non-null item labels. A label adds one unit and enters the undo stack. The reserved token UNDO reverses the most recent addition still in the stack. Ignore UNDO when empty. Remove keys whose count reaches zero and return a report ordered by label.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
