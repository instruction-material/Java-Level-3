# Parse local logs and summarize accepted events

Read UTF-8 lines in time|type|actor format. A malformed row belongs in the rejection list with its original line number. Report category counts, chronological bounds and the most common category. Break equal frequencies alphabetically. Compare an explicit counting loop with a stream.

The starter compiles and identifies the first unfinished method when checks run.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
