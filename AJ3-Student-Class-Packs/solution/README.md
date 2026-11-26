# Model a fictional student

Validate a positive student ID and a nonblank display name in the constructor. Store private state and track how many valid objects were created. The attendance flag can be changed independently on each object.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

For the optional attendance count, run java ContractTests attendance and java Main attendance.
