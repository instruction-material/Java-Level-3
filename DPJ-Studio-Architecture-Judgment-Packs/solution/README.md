# Architecture Judgment Capstone Studio

Refactor a working versioned notebook. The starter puts storage, edit rules and
auditing in NotebookDesk. Compare a helper-only cleanup with a repository seam.
The reference keeps business rules in NotebookService, collection storage in
MemoryNotes and concrete setup in Composition.

## Observable contract

IDs match `[a-z][a-z0-9-]{0,15}`. Text is non-null, at most 80 Unicode code points,
and contains neither CR nor LF. Empty text and spaces are valid and unchanged.
Null ID/text raises NullPointerException; other invalid values raise
IllegalArgumentException before state changes or audit events. Expected versions
must be positive, including for a missing ID. Calls are sequential; audit
callbacks observe committed state and do not mutate the notebook.

- create(id,text) appends a new immutable Note at version 1. Duplicate IDs raise
  IllegalStateException without changing the old note or emitting an event.
- edit(id,expectedVersion,text) returns false for a missing/stale note. A matching
  version replaces text and increments the version, even for identical text.
  Overflow raises ArithmeticException before saving. Existing position is stable.
- find(id) returns Optional.empty for an absent valid ID.
- all() returns an immutable snapshot in insertion order. Later edits cannot
  change an earlier snapshot. Each composed notebook owns independent storage.
- Successful create/edit saves first, then emits exactly one CREATED/EDITED event
  with the committed Note. Audit exceptions propagate unchanged after commit.
  This exercise has no transaction or durable storage guarantee.

## Run the baseline and refactor

Use JDK 21:

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Both Main commands print:

```text
CREATED|plan|1
EDITED|plan|2
true
false
Sketch
1
```

Trace create(plan,Read), edit(plan,1,Sketch), edit(plan,1,Stale). The first edit
replaces version 1 with version 2; the stale request changes nothing and emits
no event. Run invalid text and missing-ID cases before restructuring.

Keep LegacyNotebook and ContractTests unchanged. First extract a helper that
makes repeated validation easier to read. Then separate business rules from
collection storage through a repository port, injecting storage and audit at the
composition boundary. Update sources.txt for each added source. Preserve core
behavior after every step. Source count alone does not establish an improvement.

The reference's api and ports have no infrastructure dependency. application
depends on api/ports; memory implements the port; Composition selects both.
ArchitectureProbe compiles with only api/ports/application and a fake repository,
demonstrating storage failure, overflow and audit ordering without MemoryNotes.
It does not prove suitability for concurrent writers, SQL or durable delivery.

Record one change scenario that benefits from the seam, its added files/setup
cost, the helper-only alternative, and a condition for removing the seam.

## Optional versioned removal

Implement remove(id,expectedVersion). Validate both arguments before lookup.
Return false for missing/stale IDs. Remove a match first, then emit REMOVED with
its last version; return true. An audit failure leaves it removed and propagates
unchanged. Earlier snapshots retain it. Recreating the ID appends a fresh note at
version 1. Core is complete without this optional operation.

```sh
java -cp classes ContractTests
```

Written transfer: two clients both read version 2. Their edits use expected
version 2. In this sequential model, the first succeeds at version 3 and the
second returns false. Explain why an actual concurrent repository would need
an atomic compare-and-update contract rather than this find-then-save port.

Extension review: preserve the second note, remove the first, retry removal,
recreate the first, and compare event order and snapshot order. Explain why
audit failure after commit cannot be reported as an automatic rollback.
