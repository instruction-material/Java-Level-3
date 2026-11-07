# Editor collaboration contracts

Java 21. Keep all thirteen files and src/editor together. Main reads no input and writes no files. The model is single-threaded, in-memory, and owns no external resource.

TextDocument owns a non-null String excluding CR/LF; empty text is valid. Null inputs/results cause NullPointerException, invalid non-null data causes IllegalArgumentException unless a narrower operation contract says otherwise. snapshot creates an opaque owner-bound Memento. restore accepts only that document's snapshots and may restore the same snapshot repeatedly. It restores text only, without events, external effects or serialized persistence.

EventBus is an Observer publisher. Each subscribe creates an independent registration even for the same Consumer. Its Subscription.close removes that registration idempotently. publish rejects a null event and visits a captured registration list once in order. Subscription changes affect future captured lists; a listener removed after capture still receives that in-flight event. Reentrant publication is allowed and has its own snapshot. A listener exception propagates immediately and stops remaining listeners; earlier effects are not rolled back.

ReplaceCommand packages one edit and its snapshot. execute works only in NEW, captures current text, applies the validated replacement and moves to DONE. undo works only in DONE and only while current text equals the applied replacement; it restores the snapshot and moves to UNDONE. A rejected stale undo stays DONE and leaves text unchanged. Commands are one-shot, without redo. This is a value-based guard, not revision identity: an external same-value edit is indistinguishable. The mediator owns its document privately so its history cannot be mutated through a document getter.

Validation is an ordered Chain of Responsibility. Its copied list of non-null Checks is independent of caller list changes. check validates text before any check, calls each stage once in order, rejects a null Verdict, and returns the first rejection without later stages. Empty chain accepts. Verdict.pass has an empty reason; rejection requires a nonblank single-line reason. Stage exceptions propagate. nonBlank and maxLength are supplied; length means String.length UTF-16 code units and a negative limit is invalid.

EditorMediator coordinates validation, commands/history and events. replace rejects a validation verdict with false and no model/history/event change; otherwise it executes a command, pushes it, then publishes one REPLACE event. undo returns false when empty; otherwise it undoes the top command, pops it, then publishes one UNDO event. History is LIFO. Notifications occur after model/history commit. If a listener throws, the completed edit/undo remains committed and its history is consistent; the exception propagates. Nested replace/undo calls during any operation are rejected before changing model/history. The busy guard is released in finally, including failures. Read-only text/depth observations are allowed. No atomic external rollback or concurrency guarantee is promised.

Nodes defines immutable Text/Group records and a sealed node family. Groups snapshot child membership, preserve order/duplicates and reject null. accept dispatches to the matching Visitor operation and rejects a null visitor/result. TextVisitor renders literal unescaped bracket/comma exercise notation, recursively visiting every child occurrence. LeafCount is a separate supplied operation counting text occurrences, including empty text. Shared subtrees count once per occurrence; traversal is assumed to have a bounded depth. Adding an operation adds a visitor; adding a node type changes every visitor contract. A small direct method/switch can be simpler for a stable single operation.

Core methods: TextDocument.restore, EventBus.publish, ReplaceCommand.execute/undo, Validation.check, EditorMediator.replace/undo, TextVisitor.group. Optional EventBus.subscribeOnce invokes a registration at most once across nested dispatches, removing itself before its callback and guarding older captured lists. A thrown callback still consumes its one invocation. Closing still follows the normal captured-list lifetime rule. Core/Main work with this optional method unfinished.

    javac --release 21 -Xlint:all -Werror -d classes @sources.txt
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

The untouched starter compiles and names its first unfinished method. Keep subscription order/ownership, command phase and stale-undo traces, first-rejection chains, committed-state versus notification failures, and a two-operation visitor comparison before consulting the reference.

Main prints:
    replace:Ready->Checked
    true
    false
    undo:Checked->Ready
    true
    Ready:0
    [a,[b,c]]
    3

[Consumer](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Consumer.html)
[AutoCloseable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/AutoCloseable.html)
[Deque](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Deque.html)
