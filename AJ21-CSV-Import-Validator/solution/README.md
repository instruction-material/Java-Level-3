# Validate rows before adding them to a snapshot

Accept UTF-8 rows in id,count,status format. The file uses three unquoted fields with no embedded commas. Count is a nonnegative int. Status is READY or WAITING. Check duplicates against both existing and earlier accepted rows. Return accepted rows and specific rejection reasons in file order.

This reference passes the supplied contract checks.

```sh
javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
java ContractTests
java Main
```
