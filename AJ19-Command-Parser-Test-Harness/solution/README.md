# Command Parser Test Harness

Implement Parser.parse as a method that accepts text and returns a typed command. It has no console, file, or inventory state. The runner prints results from simple examples. The test package can call the parser independently.

Supported commands are ADD name count, PRINT, and QUIT. Strip outside space and break the line on whitespace. Command words are case-insensitive. Keep item names as given. ADD needs a one-word name and a positive int count. PRINT and QUIT take no arguments. Reject blank input, unknown verbs, extra tokens and invalid counts with IllegalArgumentException.

The sample displays Add[item=pencils, count=3] and Print[].

From the extracted project root, use Java 21:

```sh
javac --release 21 -Xlint:all -Werror -d out @sources.txt
java -cp out checks.ContractTests
java -cp out runner.Main
```
