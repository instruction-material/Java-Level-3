# DPR1: Safe refactoring workflow

This working reminder program has one fee rule copied into three public
operations. Preserve its behavior while centralizing that rule. The reference
uses the existing fee operation; it adds no strategy hierarchy or discovery
mechanism. The task begins with runnable code.

## Protected contract

Loan is an immutable record of borrower, book, lateDays and staff. Borrower is
non-null, nonblank, at most 40 Unicode code points; book has the same rules with
maximum 80. Text excludes CR, LF and pipe. Accepted text is retained exactly,
including leading/trailing spaces. Constructor validation checks borrower,
then book, then days. Null text raises NullPointerException; other invalid
values raise IllegalArgumentException.

Days is a whole int from 0 through 365. fee validates that range even for staff.
The first 3 days are free; each later day costs 25 integer cents. Staff always pay 0.
These are fictional exercise rules. Operations read no input and write no files.

- fee(days,staff) returns integer cents.
- notice(loan) returns exactly: To BORROWER: BOOK | overdue=DAYS | charge=CENTS
- ledger(loan) returns exactly: BORROWER|BOOK|DAYS|CENTS|STAFF or READER
- notice/ledger reject a null Loan with NullPointerException.
- Calls are independent and leave Loan unchanged.

| Days | Reader cents | Staff cents |
| --- | --- | --- |
| 0 | 0 | 0 |
| 3 | 0 | 0 |
| 4 | 25 | 0 |
| 12 | 225 | 0 |
| 365 | 9050 | 0 |

## Build and compare

Use JDK 21 and the complete role folder:

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes Main base
java -cp classes ContractTests core
java -cp classes Main
```

Both Main modes print:

```text
25
To Mira: Atlas | overdue=4 | charge=25
Mira|Atlas|4|25|READER
0
```

Baseline checks LegacyLoanDesk against an independently stated fee formula and
exact output templates. Core additionally checks LoanDesk across every allowed
day/staff pair, several names, invalid inputs, text preservation and repeat calls.
Both roles already pass core. Green behavior alone does not demonstrate a
structural improvement or justify a pattern.

## Small refactor sequence

Keep LegacyLoanDesk, Loan, Main and ContractTests unchanged. Capture baseline
commands/results in CHECKPOINTS.md before edits.

1. Locate the three fee calculation sites and describe their drift risk.
2. Rename one local variable consistently, compile and run core.
3. In notice, replace only the duplicated calculation with fee; run core.
4. Do the same in ledger and rerun core/Main.
5. Name the grace/rate constants and compare the source dependency map.
6. Record the observed rule locations, public API and a simpler alternative in
   DECISIONS.md. Retain the last green source separately from a failed change.

Changing the free period from 3 to 7 days is a feature change, not this refactor.
For a reader at 4 days that new policy gives 0 instead of 25; at 12 days it gives 125
instead of 225. State that changed contract separately during written transfer.

## Optional immutable batch

Implement noticeAll(List<Loan>). Snapshot the non-null list; a null element raises
NullPointerException. Return notices in input order, preserving duplicates in an
unmodifiable list. Leave the input unchanged; later input-list changes leave the
returned strings unchanged. Empty input returns an empty unmodifiable list.
The method uses the same notice contract. Core remains complete without it.

```sh
java -cp classes ContractTests
```

Extension evidence: pass [Mira/Atlas/4/reader,Sol/Maps/12/staff,Mira/Atlas/4/reader],
try changing the returned list, then clear the input. Retain exact lines,
duplicate order, immutability and unchanged core output.

Concept references: [small clean designs](https://refactoring.guru/refactoring/what-is-refactoring)
and [Extract Method](https://refactoring.guru/extract-method).
The concrete LoanDesk contract above is supplied for this exercise.
