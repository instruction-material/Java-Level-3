# Binary search trace practice

Record each midpoint probe as lower:middle:upper with inclusive bounds. Return space-separated probes with no trailing space. Include the final probe if it finds the target. An empty array gives the empty string. A missing target ends when the last failed probe empties the range. For [2, 4, 10, 27, 38] and target 28, expect `0:2:4 3:3:4 4:4:4`.

This role includes one implementation of the contract.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
