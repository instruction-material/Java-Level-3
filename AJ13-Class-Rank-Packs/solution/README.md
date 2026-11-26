# Class rank

Use a PriorityQueue to rank Student records by GPA descending, then by name ascending for tied grades. GPA is stored in hundredths, in the range 0 through 400. Poll the head repeatedly to obtain sorted output. The given input list must remain unchanged.

For the optional cutoff exercise, implement topWithTies. Include everyone tied with the student at the cutoff position. A limit of zero or less returns an empty list.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional cutoff, run `java ContractTests cutoff` and `java Main cutoff`.
