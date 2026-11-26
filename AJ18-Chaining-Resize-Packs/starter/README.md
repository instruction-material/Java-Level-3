# Delete and rehash entries in a chained table

Use one list of entries per bucket. Remove a matching entry and decrement the count once. When the load factor exceeds one-half, rebuild the bucket list and route every entry to the new index. Updating an existing key keeps one entry.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java HashMap.java ContractTests.java
java ContractTests
java Main
```

Compile Main.java, HashMap.java and ContractTests.java together.
