# Batch intake with a queue and map

Read a batch of document IDs. Use a map to count each ID and enqueue only the first occurrence. Return dispatch summaries in first-arrival order. IDs are non-null and case-sensitive. Leave the arrival list unchanged.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
