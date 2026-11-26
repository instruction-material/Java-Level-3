# Map lookup extension practice

Build a catalog from inventory updates. Trim Java whitespace around the SKU and lowercase it with Locale.ROOT before storage and lookup. Reject a blank SKU and negative quantities. If multiple updates name the same SKU, the latest count replaces the earlier count. Return an empty OptionalInt for a missing key. Zero is a valid count.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
