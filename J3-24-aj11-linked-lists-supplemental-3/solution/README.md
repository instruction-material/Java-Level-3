# Reverse an existing linked chain

Reverse all next links in a singly linked chain and return its new first node. The input head is a data node or null. Save the next pointer before redirecting the current node. Preserve the actual node objects, use constant additional storage and guarantee that the old first node becomes the last.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
