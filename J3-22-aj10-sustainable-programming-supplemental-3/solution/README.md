# Collect valid readings and their error positions

Process independent temperature-record lines. Each line is a finite double in the inclusive range -40 to 60. Valid values are returned in input order. Record one-based line numbers for missing or invalid values, and continue processing the rest of the batch. Return both lists in a Report. Keep the validation rule in the supplied read method and call it once per row.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
