# Trace a fare calculation

Compute a fare of two base tokens plus three per zone. Zero zones still cost two tokens. Reject a negative zone count and return a long.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
