# Extract report-building stages

Keep the existing output while extracting the parse, aggregate and format stages. Each row has a name and a nonnegative integer quantity separated by a colon. Keep invalid rows out of the totals and report the number skipped. Use a lexically ordered report.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```

The starter runs and prints the original report. Save that output first. Once the stages pass, rerun java Main and compare its output with the saved result.
