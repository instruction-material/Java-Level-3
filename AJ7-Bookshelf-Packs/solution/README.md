# Biggest to Smallest Bookshelf

Return a separate list of books in descending page-count order. Preserve original order for equal page counts and keep each title attached to its page count. For each output step, select the first largest entry from a working ArrayList and remove that entry from the copy. Leave the caller's list unchanged.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
