# Valid parentheses

Return true if the given text consists only of correctly nested parentheses, brackets and braces. Empty text is valid. Reject unexpected characters and null. Use a character stack whose capacity grows with the input and match closing symbols against the latest unmatched opening symbol.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
