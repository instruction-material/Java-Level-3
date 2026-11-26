# Check-In 1 workbook

Use four pieces of evidence to review recursion, growth, linear search and binary search.

- Return the UTF-16 length of a short non-null string without calling length in the method. An empty string gives 0. Use a base case and recurse on the suffix. Repeated suffix copies can use quadratic work.
- Return Pascal's triangle at a zero-based row from 0 to 20. An out-of-row column gives 0. Valid row 0, column 0 gives 1. Use the two parents and trace row 4, column 2, which gives 6.
- Explain which operation grows for `25n² + 20n + 15n³`, `log(n) + sqrt(n)` and `1 × 2 × … × n`. The third expression describes a factorial value. A procedure using one multiplication for each factor after the first has linear counts.
- Implement linearContains for any non-null int array. Implement binaryIndex and binaryRange for ascending arrays. Test a first-position match, a final-position match, an absent value and an empty array. Binary search may return any matching duplicate. For a miss, explain how each unfinished search reaches its stopping condition.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

Run `java Main trace` to see the original recursive call order. Write the prediction before looking at the console.

For the optional memo extension, reopen this workbook. Use the supplied zero-filled table with row + 1 rows and columns. A stored positive result can be returned on a later call. Run `java ContractTests memo` and `java Main memo`.
