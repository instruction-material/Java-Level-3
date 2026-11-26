# Priority queue transfer practice

Order repair jobs by urgency descending and then arrival number ascending. Arrival numbers are unique and encode the original order. Build the output by removing the head, copying its label to the result and repeating until the queue is empty. Keep the input order and values intact.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
