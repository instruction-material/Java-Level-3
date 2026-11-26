# Split an array into recursive leaves

Collect the leaves of the split tree as singleton lists of int values from left to right. An empty input has no value leaves. Treat [start, end) as a half-open range. Once the range contains one element, record that value and return. The task demonstrates the split phase and leaves the original order intact.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
