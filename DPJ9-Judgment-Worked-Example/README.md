# Smaller refactor or selected behavior
Java 21:
```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
Main prints NOTICE: HELLO three times, once per implementation.

Legacy duplicates the format rule in banner and email. Direct centralizes that fixed rule
and retains both entrypoints. Pluggable receives a function, introducing a field and constructor
and moving behavior selection to composition. It earns that flexibility only when independently
selected behavior is needed. The main outputs stay equal; a custom injected rule deliberately
has a different contract. Null/CR/LF text is rejected, whitespace is trimmed and case conversion
uses Locale.ROOT. The injected variant validates its result and calls its formatter once.
Empty text is valid. Keep behavior equivalence distinct from evidence about coupling, duplication,
added machinery and whether a concrete variation justifies it.
