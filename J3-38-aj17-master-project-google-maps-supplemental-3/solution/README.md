# Audit one-way and asymmetric roads

Report each directed road that has no reverse road or whose reverse cost differs. Emit each directed finding in source and destination order. Equal reverse costs are symmetric. The map contains nonnegative whole-mile costs.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
