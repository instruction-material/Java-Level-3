# Trace a room-owned desk

A Code record stores a reusable identifier. It has no room instance. Each Desk belongs to one Room and displays the room's current name followed by the desk number. Desk numbers must be positive.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
