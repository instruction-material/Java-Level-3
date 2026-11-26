# Check-In 2 sorting workbook

Work through the selection, insertion and merge traces, then complete the matching methods. Use selection on [34, 23, 76, 75, 14, 25] for two passes, insertion on [99, 15, 27, 13, 8, 1] for three passes. Merge [1, 4, 7, 10] and [2, 3, 5, 11] with the supplied cursor merge, then implement the recursive caller.

Return a separate array in each case and leave the input alone. Pass counts are nonnegative. Requests beyond the useful pass count finish the sort. Explain the fixed prefix or tail after each pass. Full sorting examples must preserve each input value, including duplicates.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the equal-run extension, run `java ContractTests range` and `java Main range`. The result is [first, endExclusive] in the sorted copy; an absent value gives [-1, -1].

For the optional bubble trace in the reference archive, use [50, 23, 12, 43, 12, 20] and two passes. Run `java ContractTests bubble` and `java Main bubble`.
