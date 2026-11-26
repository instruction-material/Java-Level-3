# Merge sorted attendance IDs

Produce the ascending union of the IDs from two sorted attendance lists. Preserve the inputs. Emit each distinct ID once, whether it appears once or repeats within or across the lists. Use one cursor per input and compare each emitted candidate with the most recent result. All int values, including Integer.MIN_VALUE, are valid IDs.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
