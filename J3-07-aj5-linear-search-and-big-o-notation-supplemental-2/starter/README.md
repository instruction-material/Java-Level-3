# Search Runtime Transfer Practice

Return the first zero-based position whose whole label starts with the supplied prefix, or -1 if none do. Use case-sensitive matching. Accept an empty label array. Require a non-null array of non-null strings and a nonempty prefix. A null or empty prefix throws IllegalArgumentException.

Compile this folder independently on JDK 21.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

Main prints `0`, `1` and `-1` on separate lines.
