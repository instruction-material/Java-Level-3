# Recover from invalid measurements

Return valid integer measurements in order and report the line numbers of the invalid ones. Values must be integers from 0 to 100. The parser declares a checked exception for an invalid line. The batch caller handles each line independently.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
