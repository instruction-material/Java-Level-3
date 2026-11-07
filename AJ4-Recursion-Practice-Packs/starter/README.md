# Learner starter

# Recursive methods

Implement the five marked methods in `Main.java`. Keep the supplied signatures, driver and tests. Each method uses a base case and a recursive call on a strictly smaller input.

| Method | Contract | Small checks |
| --- | --- | --- |
| `factorial(n)` | `0 <= n <= 12`; return `n!` as an `int`. Reject other inputs with `IllegalArgumentException`. | `0 -> 1`, `1 -> 1`, `5 -> 120`, `12 -> 479001600` |
| `exponent(b, p)` | Any signed `int` base and `0 <= p <= 31`. The empty product is 1, including `exponent(0, 0)`. Reject an invalid power with `IllegalArgumentException`; throw `ArithmeticException` if the exact result cannot fit an `int`. | `(2,3) -> 8`, `(-2,3) -> -8`, `(0,0) -> 1`; `(2,31)` overflows |
| `sumDigits(n)` | Accept every signed `int`; return the sum of the decimal digits of its magnitude. | `0 -> 0`, `1234 -> 10`, `-1234 -> 10`, `Integer.MIN_VALUE -> 47` |
| `fibonacci(n)` | Preserve the original one-based project sequence: term 1 is 0, term 2 is 1. Accept `1 <= n <= 31`; reject other terms with `IllegalArgumentException`. | `1 -> 0`, `2 -> 1`, `5 -> 3`, `10 -> 34` |
| `palindrome(word)` | Accept a non-null ASCII string of at most 256 characters. Compare exact characters, including spaces, punctuation and case. Reject other inputs with `IllegalArgumentException`. | empty and `racecar` are true; `ab` and `Aba` are false |

The bounds keep these introductory recursive methods within a small call stack and bounded runtime. The Fibonacci method intentionally exposes repeated subproblems; its limit is a teaching boundary, not an `int` capacity claim. A memoized or iterative version is a later extension.

## Method and trace

For factorial, use `0! = 1` and `n! = n * (n-1)!`. For nonnegative powers, use an empty product of 1 and remove one factor per call. `Math.multiplyExact` reports integer overflow instead of silently wrapping.

For digit sums, separate the last digit with `% 10` and remove it with `/ 10`. Widen an input to `long` before taking the magnitude of `Integer.MIN_VALUE`; its positive magnitude cannot fit in an `int`, but the remaining digits do.

For Fibonacci, terms 1 and 2 are the two base cases; later terms sum the preceding two. For the ASCII palindrome, zero or one character is a base case; compare the outer characters, then narrow the string from both ends.

A call `factorial(4)` descends through `4, 3, 2, 1`. The returns are `1, 2, 6, 24`. Each pending frame remembers the multiplication still to perform. Write a similar call-and-return trace for each method and identify what decreases.

## Build and check

Use JDK 21 in this folder:

```text
javac -Xlint:all Main.java ContractTests.java
java ContractTests
java Main
```

Continue to `java` only after compilation succeeds. The unfinished starter compiles and then throws a named implementation reminder. After all methods are implemented, the supplied checks print `Practice contracts: 368 passed`.

Tests compare outputs and error boundaries. Submit a recursive call/return trace and decreasing-measure explanation as well; passing value checks alone does not demonstrate a recursive implementation.

The driver prints:

```text
Factorial of 5: 120
2^3: 8
Sum of the digits 1234: 10
5th Fibonacci number (term 1 is 0): 3
Is "racecar" a palindrome? true
```

Keep the saved learner attempt when comparing the separately linked reference.
