# Compare scheduling policies

Order immutable tasks by deadline, descending importance, duration, or an injected comparator. Return task IDs in next-job order. Use ID as the last tie-breaker and compare a sorted copy with the queue's poll order.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
