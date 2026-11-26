# Unlink only the first match

Remove the first data node equal to a target from a chain with a sentinel head. Return true when a node was removed and leave the rest of the chain in order. Return false on a miss. Use Objects.equals so a null data value can match.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
