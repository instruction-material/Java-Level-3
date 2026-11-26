# Trace the sorted tail after each pass

Return a copy of the numeric array after each shrinking-bound bubble pass. Complete all scheduled passes so the result contains n−1 snapshots for nonempty input. Each snapshot is separate and the original array is unchanged.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
