# Implement an open-addressed hash map

Complete bounded linear probing and rebuild the map at a new capacity. Distinguish never-used slots from tombstones. An insertion remembers a deleted slot but searches beyond it for an equal live key. Reject duplicate inserts, update present keys with put, and reject missing get, remove and put. Optional compact mode clears tombstones without changing capacity.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```

For optional compaction, run `java ContractTests compact` and `java Main compact`.
