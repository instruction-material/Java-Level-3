# Find the first passing trial

Each boolean records whether the system passed at a trial. The dataset contains failures followed by passes, possibly with one group empty. Return the index of the first true, or -1 if nothing passes. Use a half-open search range to find the boundary. Each probe answers whether the selected trial passed. Trace [false, false, true, true]. Which range remains after probing index 2?

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
