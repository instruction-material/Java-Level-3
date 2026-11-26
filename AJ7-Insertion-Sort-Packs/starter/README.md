# Insertion sort

Sort the int array in place, shifting larger prefix values right before inserting the saved value. Return the count of shifts. Count each shifted value and exclude writing the saved value to its final position. Equal values do not shift past each other. For [4, 1, 3, 1], the sorted array is [1, 1, 3, 4] after four shifts.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
