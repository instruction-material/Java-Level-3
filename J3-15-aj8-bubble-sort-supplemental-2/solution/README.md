# Trace the sorted tail after each pass

Return a copy of the numeric array after each shrinking-bound bubble pass. Complete all scheduled passes so the result contains n−1 snapshots for nonempty input. Each snapshot is separate and the original array is unchanged.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
