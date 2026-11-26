# Precise square roots

Approximate square roots for finite nonnegative doubles within two units of the last place of the correctly rounded result. Preserve signed zero. Reject negative or nonfinite values with IllegalArgumentException. Start the upper bound at max(1, n). For a positive midpoint, compare middle to n / middle to avoid overflow. Stop when there is no representable double between the two bounds. Use Math.sqrt as a check. The console accepts a number as its argument, for example `java Main 0.25`.

This role includes one implementation of the contract.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
