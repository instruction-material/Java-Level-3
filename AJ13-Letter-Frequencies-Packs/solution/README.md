# Letter frequencies

Count Unicode letters in a sentence. Read by code point, ignore nonletters and use Character.toLowerCase for each letter. Return a map sorted by the numeric code point. On a tie, choose the smallest code point. Empty or punctuation-only input has no result. This exercise counts code points and uses simple case conversion for each code point independently.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
