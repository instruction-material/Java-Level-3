# Read the first usable count

Read complete lines until a trimmed nonnegative int appears. Accept zero. Reject malformed, negative, and out-of-range lines. Return an empty result when the text ends.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
