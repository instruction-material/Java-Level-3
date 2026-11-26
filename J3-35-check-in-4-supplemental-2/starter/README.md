# Verify a hash migration snapshot

Given live before and after bucket entries, verify the same key and value pairs appear once. Each entry in the new table must be in the bucket selected by the new floorMod. A new table with no buckets and any duplicate live key are invalid.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
