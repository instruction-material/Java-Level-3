# Bounded Task Runner

Use one to eight workers and at most that many outstanding jobs at a time. A Job snapshots its input values. Each worker returns its own outcome and execution time. The collector retrieves futures in input order to keep the report stable. A checked failure is recorded for that job while other results remain available. If the collecting thread is interrupted, propagate the interruption and stop remaining worker tasks in cleanup.

The sample prints oak 5, cedar Negative value, and elm 0. The contract harness also makes the later job advance before the earlier one and checks that report positions stay in input order. Timing is recorded but omitted from the reproducible console output.

Compile from this folder with Java 21:

```sh
javac --release 21 -Xlint:all -Werror -d out @sources.txt
java -cp out checks.ContractTests
java -cp out runner.Main
```
