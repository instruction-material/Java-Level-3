# Index a local keyword corpus

Load the supplied UTF-8 corpus into the custom HashMap. A line containing only an HTTP or HTTPS address starts a page. Subsequent nonblank lines supply whitespace-separated tags until the next address. Lowercase tags and queries with Locale.ROOT, ignore empty tags, and retain each matching address once in corpus order. A tag line before any page address is invalid. All addresses are data and the program makes no network requests.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror *.java
java ContractTests
java Main
```

Run `java Main corpus.txt JAVA tools` to select a local file and keywords. Keep corpus.txt beside the Java sources.
