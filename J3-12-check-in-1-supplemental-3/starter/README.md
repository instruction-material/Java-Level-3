# Count visits in nested loops

The outer loop runs n times. The middle loop runs floor(n / 4) times for each outer step, and the inner loop runs twice per middle step. Implement visits to count the exact number of inner-body executions using a formula and long arithmetic. Return zero for small sizes with no middle iterations. Reject negative sizes. Explain the growth order of the original loops and of the formula.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
