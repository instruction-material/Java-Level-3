# Complete reference

# Recursive divisibility by seven

Implement `boolean isDivBySeven(int n)` in `Main.java`. Every signed 32-bit integer is accepted, including zero, negatives and `Integer.MIN_VALUE`. Return whether it is divisible by 7.

The driver reads whitespace-separated integer tokens until EOF or `quit`. A token outside the signed `int` range, or a non-integer token, prints `Invalid integer.` and leaves later tokens available. Input after `quit` is ignored.

## Reduction rule

For the nonnegative magnitude, write `n = 10a + b`, where `a` is everything except the last digit and `b` is the last digit. Replace the number with `a - 2b`, then test the magnitude of that reduced value recursively.

Why this works: `10(a - 2b) = n - 21b`. The right-hand side differs from `n` by a multiple of 7. Multiplication by 10 does not introduce or remove a factor of 7. Thus the reduction preserves divisibility.

Use a magnitude below 10 as the stopping case: only 0 and 7 are divisible by 7. If the magnitude is at least 10, the reduced magnitude is strictly smaller. Explain that decreasing quantity as well as the base case.

Examples:

```text
203 -> 14 -> -7 -> magnitude 7 -> true
201 -> 18 -> -15 -> magnitude 15 -> -9 -> magnitude 9 -> false
-49 -> magnitude 49 -> -14 -> magnitude 14 -> -7 -> true
```

Widen the original `int` to `long` before taking its magnitude. `Math.abs(Integer.MIN_VALUE)` overflows when it is evaluated as an `int`. After the first digit reduction, the result fits in `int`.

## Build and check

Use JDK 21 in this folder:

```text
javac -Xlint:all Main.java ContractTests.java
java ContractTests
java Main
```

Continue to `java` only after compilation succeeds. The unfinished starter compiles and then throws a named implementation reminder for a valid test input. A complete implementation prints `Divisibility contracts: 220005 passed`.

The tests use direct remainder checks as an independent numerical oracle. The submitted method itself uses the recursive reduction. Include call/return traces for 203, 201 and -49; value tests alone do not prove recursion.

For input `0 -7 14 15 203 -203 quit 7`, the driver prints:

```text
0 is divisible by 7
-7 is divisible by 7
14 is divisible by 7
15 is NOT divisible by 7
203 is divisible by 7
-203 is divisible by 7
```

Keep the saved learner attempt when comparing the separately linked reference.
