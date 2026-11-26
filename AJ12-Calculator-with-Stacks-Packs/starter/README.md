# Calculator with stacks

Take one whitespace-separated token at a time. A signed integer pushes a value. The five binary operators use the second value from the top as the left operand and the top value as the right operand. Return false for incomplete operations, divide or remainder by zero, and invalid tokens. All failures leave the stack unchanged. Examples use Java int arithmetic, including division truncated toward zero.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
