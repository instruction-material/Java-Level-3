# Runtime Comparison Extension Practice

Return the first matching index and the actual count of equality comparisons for any non-null int array. A miss returns index -1. Count one comparison per element examined. Empty input returns (-1, 0). Keep the input unchanged.

Compile this folder independently on JDK 21.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

The four columns are size, first-hit comparisons, last-hit comparisons and missing-value comparisons.
