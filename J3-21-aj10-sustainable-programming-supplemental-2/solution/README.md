# Parse a stock entry once

A stock entry has a name and a nonnegative integer amount separated by a vertical bar. Implement a pure parser returning Stock. Strip padding from both fields. Reject missing separators, multiple separators, empty names and invalid or negative amounts with a clear IllegalArgumentException. Keep parsing independent of the console.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
