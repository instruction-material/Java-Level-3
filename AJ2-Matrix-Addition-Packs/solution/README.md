# Add matrices by coordinate

Add corresponding cells into a new matrix. Both inputs must be rectangular and have the same shape. Reject cells whose sum cannot fit in int.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
