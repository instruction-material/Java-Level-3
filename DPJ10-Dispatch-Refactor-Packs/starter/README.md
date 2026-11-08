# Dispatch desk capstone
Use Java 21. The starter is a runnable refactoring assignment and already passes core behavior. Source review and DECISIONS.md establish structural improvement beyond that starting point.

Preserve LegacyDesk, domain values, DeskApi, Main and ContractTests. Baseline and target instances have separate storage. The program reads no input, writes no files and retains data only in memory. EMAIL/SMS are simulated formats; no network messages are sent.

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Both Main modes print:
```text
EMAIL: Hi
sent:a
200
sent:b
[a, b]
```

## Observable contract
Request is immutable: id matches [a-z][a-z0-9-]*, non-null text excludes CR/LF (empty allowed), channel EMAIL/SMS, priority NORMAL/URGENT, copies 1..20. Invalid key/range/line raises IllegalArgumentException; null fields raise NullPointerException. Dispatch validates its id, line and nonnegative cents.

Normal EMAIL costs 100 per copy; normal SMS costs 60. Urgent adds 40 per copy. Rendered text is EMAIL: or SMS: followed by exact input text, with URGENT prepended for urgent requests. Preserve whitespace, punctuation and case. The independent oracle enumerates all 240 combinations of copies, channel, priority and three supplied texts.

preview computes without storing or notifying. send rejects duplicate id before rendering, computes, inserts, then invokes the non-null NoticeSink once. Notification failure propagates after storage commits; retrying that id is a duplicate. A send from a callback is rejected before nested mutation; read-only preview/find/all remain available. The guard releases after failure. This is synchronous single-threaded code without a cross-component transaction.

find validates its key and returns Optional.empty for absence. all returns an immutable insertion-order snapshot; later insertions leave old lists unchanged. Records contain immutable strings/primitives. Each new desk starts empty.

## Diagnose actual source
The initial Desk duplicates rendering/rate decisions in preview and send, mixes state ownership with use-case ordering, and ties computation changes to orchestration. Trace changing one channel's rate or prefix in both entrypoints, swapping storage and testing a throwing collaborator. Identify three to five concrete pressures and the costs of a proposed remedy.

The reference separates a Renderer strategy, DispatchRepository, NoticeSink, service and composition root. Its stable two-channel switch remains ordinary code; a factory hierarchy or event bus requires additional justification. The callback is synchronous notification, without subscription management, asynchronous delivery or rollback. This is one justified design, rather than a required class count.

The reference also supplies ArchitectureTests: run `java -cp classes ArchitectureTests` to observe injected render/store/sink ordering and failures. Its service compiles with domain/ports alone, excluding infrastructure and LegacyDesk. Composition chooses MemoryDispatches and StandardRenderer. Trusted replacement adapters must honor their contracts. A store exception may leave adapter-specific effects; the service supplies no rollback and skips notification when insert throws.

## Evidence and purpose
Follow CHECKPOINTS.md. Keep behavior-preserving commits after export, matching checks, dependency maps, defined before/after counts, a replacement demonstration, simpler rejected design, one pattern deliberately not used and rollback identities in DECISIONS.md. Count the retained legacy separately.

This unit selects and justifies collaboration boundaries in a complete application. The follow-up refactoring course revisits named transformation families and risk-ordered techniques as a separate learning purpose.

## Optional batch
After core, implement sendBatch on the same desk. Reject nested batch calls, including empty ones, during a send. Copy the non-null list and reject null elements before sending anything. Process the copy in order through send; return immutable results. Empty input returns an empty list. Stop and propagate a later duplicate/render/store/notification failure; already committed prefix items remain. Notification failure leaves the failing item's insertion committed too.

```sh
java -cp classes ContractTests core
java -cp classes ContractTests
```

The initial target inherits an explicit unfinished optional method; core works independently. The reference completes batching. A callback mutating the original input list cannot change the captured sequence.
