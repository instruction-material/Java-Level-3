# DPR4: value data and a compatible transport boundary

Use JDK 21. This pack refactors an ordered supply request represented by a destination, an integer service code and parallel item/quantity arrays. LegacySupplyRequest is the preserved baseline; ClientCompatibility is an existing client. Retain both files unchanged.

## Contract

A destination has 1–12 ASCII uppercase letters. Item codes have 1–8 ASCII uppercase letters/digits, beginning with a letter. Values remain exact and case-sensitive, without trimming. A quantity is 1–999. There are 0–64 ordered lines. Duplicate codes remain separate lines.

Service codes 0, 1 and 2 mean STANDARD/180 minutes, RUSH/60 minutes and EXPRESS/30 minutes. ServiceLevel already centralizes that classification. SupplyRequest's existing constructor, destination(), serviceCode(), dispatchMinutes(), itemCodes(), quantities(), toLegacy(), equals() and hashCode() remain callable by ClientCompatibility.

The constructor validates destination, service, array shape, then each code before its quantity. Invalid arguments raise IllegalArgumentException with message destination, service, items, code or units in that order. It owns input arrays and returns independent arrays. Both value equality and the current hash calculation retain destination, service, line order, codes and quantities.

The transport is exactly DESTINATION|SERVICE|CODE:UNITS;CODE:UNITS. It always has three fields. LAB|0| is a valid empty request. Service uses a single 0/1/2; quantities use canonical decimal digits without leading zeros, signs or whitespace. Missing/extra fields, empty entries, invalid values and null input raise IllegalArgumentException("row"). Encoding a null request raises IllegalArgumentException("request").

## Core work

1. Run the green base mode. Record the outputs and one rejected input before editing.
2. Finish ItemCode and RequestLine compact-constructor validation. Their record equality uses immutable components.
3. Replace SupplyRequest's LegacySupplyRequest field with a destination, ServiceLevel and owned List<RequestLine>. Keep all existing client methods. Implement fromLines and lines with an ordered unmodifiable snapshot. Reject a null service with "service"; reject null/oversized lists or null elements with "lines".
4. Convert the old arrays at the constructor boundary. Keep old getters as independent arrays. A record containing an array would still expose mutable contents and use array identity for equality.
5. Put old-format decoding and encoding in RequestCodec. Preserve the external bytes and rejection contract; keep transport delimiters out of domain behavior.
6. Run base and core after each structural checkpoint. Retain ClientCompatibility and the baseline source. Document why a value type, enum and snapshot each earn their place.

## Commands and complete supplied example

From either complete role folder:

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Base prints 1673 passed. The completed core prints 1704 passed; the complete reference including optional merge prints 1709 passed.

The starter compiles, passes base, and prints the example. Its core mode intentionally fails on unfinished typed-value work. The reference passes base/core. Both Main modes print exactly:

```text
LAB|1|PEN:2;PAD:1;PEN:3
LAB dispatch=60 lines=3 units=6
equal=true
hash-set=true
unchanged=LAB|1|PEN:2;PAD:1;PEN:3
```

No standard input, external service, package download or data file is required. Keep all Java files and sources.txt when exporting. In the site IDE, Run shows the native Java commands; run them from the exported role folder.

## Optional behavior change

After core is green, implement merged(). Combine equal codes into one line, retaining each code's first-occurrence order. Return a new request; retain the original. Reject a combined quantity above 999 with IllegalArgumentException("units"). Empty requests remain empty. This deliberately changes line grouping and is assessed separately from refactoring.

```sh
java -cp classes ContractTests
java -cp classes Main merged
```

The merged example adds: merged=LAB|1|PEN:5;PAD:1.

## References

- [Java 21 records](https://docs.oracle.com/en/java/javase/21/language/records.html)
- [Java 21 List.copyOf](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))
