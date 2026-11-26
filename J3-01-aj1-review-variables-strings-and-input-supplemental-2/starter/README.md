# Trace a fare calculation

Compute a fare of two base tokens plus three per zone. Zero zones still cost two tokens. Reject a negative zone count and return a long.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
