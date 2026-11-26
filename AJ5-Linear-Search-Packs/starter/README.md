# Linear search and repeated matches

Return the zero-based index of the first equal int, or -1 when absent. Accept a non-null array, including an empty one, and any int target. Reject a null array with IllegalArgumentException. The optional extension returns every matching index in ascending order. Both methods leave the array unchanged.

Compile this folder independently on JDK 21.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

After the optional extension, run `java ContractTests all` and `java Main all`. The second command prints `[0, 2]`.
