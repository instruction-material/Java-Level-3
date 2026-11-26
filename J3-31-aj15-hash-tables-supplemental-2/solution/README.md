# Hash table collision transfer practice

Use separate chaining. Each bucket contains a list of entries. Replace the value for an equal key; leave unequal colliding keys intact. Compute the bucket with floorMod and return OptionalInt.empty for a missing key.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
