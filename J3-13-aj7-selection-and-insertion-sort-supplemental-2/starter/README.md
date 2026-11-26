# Stable insertion for labeled tasks

Return a copy of a task array ordered by ascending priority. Keep equal priorities in arrival order and keep the caller's array intact. Use insertion sort and shift an earlier task only when its priority is strictly higher than the saved task's.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
