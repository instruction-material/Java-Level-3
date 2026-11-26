# Reverse and alphabetize a queue

Build separate queue snapshots for the initial, reversed and alphabetized orders. Use a stack to reverse the entire queue and a priority queue to alphabetize using String's natural order. Preserve duplicate names and leave the source list untouched.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
