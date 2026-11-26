# Handle file, input and arithmetic failures

Implement parse, startsWithVowel, wordCount and divide. Strip surrounding whitespace before parsing a signed decimal int. Missing, blank, malformed or out-of-range input raises NumberFormatException. A null or empty word returns false. Recognize uppercase and lowercase ASCII vowels. Read the supplied sample.txt as UTF-8 and count whitespace-delimited words. Let IOException reach the caller. Divide with a fractional result and throw the supplied checked exception for zero denominators.

Run java Main with optional arguments integer, denominator and input filename. The default file contains three words. Input 5 and denominator 2 produce 2.5. The console identifies input, I/O or division errors using separate catch blocks.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
