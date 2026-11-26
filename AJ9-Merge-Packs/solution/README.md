# Merge two sorted arrays

Combine two ascending arrays into a fresh ascending result, preserving duplicates and both inputs. Track one cursor for each input. If the current values are equal, take the left one first. Once one side is empty, copy what remains. The optional record extension applies the same traversal to labeled entries and compares only their keys.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional records task, run `java ContractTests stable` and `java Main stable`.
