# Graph modeling extension practice

Report incoming and outgoing counts for the declared vertices and any endpoints named by the data. Count repeated ordered arcs once and count a self-loop once in each direction. Return a map sorted by vertex label and preserve the original data.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
