# Calculate and classify a score average

Average scores from 0 to 100 using floating-point division. An empty record has no mean. Classify the unrounded mean at the 90, 80, 70, and 60 cutoffs.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
