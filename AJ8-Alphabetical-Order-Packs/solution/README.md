# Alphabetical order from a word list

Read the supplied words.txt from the project folder as UTF-8. Use whitespace to separate words. Sort a mutable list using adjacent comparisons and String.compareTo. Keep duplicates. The comparison uses Java's case-sensitive UTF-16 order, so uppercase English letters precede lowercase English letters.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
