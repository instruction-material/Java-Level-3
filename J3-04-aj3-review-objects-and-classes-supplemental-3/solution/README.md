# Calculate fees through an abstract type

Use an abstract Parcel with a weight in whole units. A standard parcel costs 100 cents plus 25 per unit. An express parcel has the same base and costs 50 per unit. Reject negative weight.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
