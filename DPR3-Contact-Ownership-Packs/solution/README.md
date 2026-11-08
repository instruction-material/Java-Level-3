# Moving features between objects

Use JDK21 with the complete role folder and package layout. The starter is runnable. The core work moves data and behavior while preserving callers, validation and state transitions. LegacyMember and LegacyPrinter remain an unchanged baseline. ClientCompatibility.java remains unchanged and compiles against both target designs.

## Protected contact contract

Member(name,area,number) validates name first, then area, then number. Name is non-null, nonblank according to String.isBlank, at most60Unicodecodepoints and contains no CR, LF, < or >. Area is exactly2..4ASCIIdigits. Number is exactly4..8ASCIIdigits. Null raises NullPointerException; other invalid input raises IllegalArgumentException. Preserve valid text exactly, including name whitespace and leading numeric zeros. Getter returns retain the supplied String identities.

displayName, areaCode and localNumber return the current fields. telephone returns exactly (AREA) NUMBER. ContactPrinter.telephone(Member) preserves the old public entrypoint and the same output. ContactPrinter.label(Member), also called by ClientCompatibility.render, returns exactly NAME <(AREA) NUMBER>. Printer methods reject null Member with NullPointerException.

changePhone validates the next area then next number before committing either field. On failure both old fields remain. Success replaces both; repeated identical updates are valid. Name stays fixed. Calls perform no external effects and retain no shared static state. The model is sequential, without a concurrent-access guarantee. These are supplied exercise formatting rules.

## Build and actual refactoring steps

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Both Main modes print:
```text
(212) 5550100
Mira <(212) 5550100>
Mira <(020) 70001234>
020|70001234
2024-01-08
2024-01-08
```

First retain the old ContactPrinter entrypoint and delegate formatting to Member.telephone. Compile and run before moving fields. Then extract immutable Phone, move area/number validation and formatting into it, and keep Member's old getters as delegates. Construct a replacement Phone before assigning it. The old client compiles without knowing the new representation. The reference has14files; starter13. Adding Phone requires adding src/contacts/Phone.java to sources.txt. Record real diffs, commands and recovery points in CHECKPOINTS.md.

The model compiles without printer or client classes. Show which files change for a phone-format rule versus a member-name rule. Compare the flat model, method-only move and extracted owner, including added setup and a smaller alternative. Public compatibility is checked through the actual unchanged ClientCompatibility source. Design judgment is explained separately from green behavior checks.

## Complete library-friction comparisons

ForeignDateMethods.followingMonday(LocalDate) returns the first Monday strictly after the input, including a7dayadvance for Monday. Null raises NullPointerException. LocalDate range overflow raises DateTimeException. Inputs are immutable and remain unchanged.

BusinessDate is a composition-based local extension around the same immutable LocalDate; date() retains that reference and followingMonday() returns a new wrapper for the same result as the foreign helper. A single missing operation can justify a static foreign helper; repeated coherent operations may justify a wrapper and its interoperation cost. Neither example changes the JDK class. Java already supports the direct expression date.with(TemporalAdjusters.next(MONDAY)); compare that first. The helper and wrapper demonstrate domain naming and wrapping costs. A 400-date oracle compares both against the standard operation.

## Optional masked format

After core, implement Member.maskedTelephone(). It returns (AREA) followed by a space, one*pernumbercharacter except the last2, then the last2digits. For212/5550100 it is (212) *****00. Preserve the full number and ordinary output. After a valid update, mask the new number. The starter throws a named UnsupportedOperationException only for this optional operation; the reference delegates the rule to Phone.masked.

```sh
java -cp classes ContractTests
```

Core remains complete without masking. Keep the full number private to the existing caller contracts; the optional output is another stated exercise representation.
