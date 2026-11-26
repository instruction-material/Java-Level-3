# Read the first usable count

Read complete lines until a trimmed nonnegative int appears. Accept zero. Reject malformed, negative, and out-of-range lines. Return an empty result when the text ends.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
