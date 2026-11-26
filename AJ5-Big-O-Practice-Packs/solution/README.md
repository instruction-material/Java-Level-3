# Big-O practice

For positive integer input sizes, enter the tight growth label for each expression. Use the listed label format. For the two-variable expression assume n and m can vary independently. In the final recurrence, integer division halves n.

Compile this folder independently on JDK 21.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java Main
java ContractTests
```

The harness checks row completion and label spelling. Explain why each label fits the expression. It does not grade the reasoning.

Accepted labels: `1`, `log(n)`, `n`, `n log(n)`, `n^2`, `log(n) + m^2`.
