# Trace split ranges

Record the half-open ranges entered by recursive splitting in left-to-right, call-entry order. Include singleton ranges. The empty input yields no trace. For size 3, visit [0,3), [0,1), [1,3), [1,2), [2,3). Reject a negative size.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
