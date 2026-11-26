# Check proposed routes against a graph

Choose the least costly valid route from the supplied candidates. Arcs are directed and costs are nonnegative long values. Ignore candidates with a missing link. Use exact addition and reject a candidate whose total does not fit in long. Preserve input order to break equal-cost ties. The supplied candidates are the comparison set.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
