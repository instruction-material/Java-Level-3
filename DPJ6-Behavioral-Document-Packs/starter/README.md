# Behavioral document contracts

Java 21. Keep all eleven files and src/behavior together. Main reads no input and writes no files.

Text is a non-null single-line String; CR/LF cause IllegalArgumentException. Empty text is valid. Null inputs/results cause NullPointerException. The caller selects a TextRule (Strategy) at RuleContext construction. format validates input before calling that rule once, validates its result, and propagates a rule exception without retry. Identity, Upper with Locale.ROOT, Prefix and String::strip demonstrate distinct choices. A class fits named captured state; a functional interface permits a small lambda or method reference.

Document owns private phase objects (State), initially DRAFT. DRAFT permits replace and submit to REVIEW. REVIEW permits revise to DRAFT or approve to PUBLISHED. PUBLISHED permits no transitions or replacement. Every other operation throws IllegalStateException without changing document state/text. replace validates text before phase dispatch. Documents have independent state; shared enum phase objects hold no mutable per-document fields. Phase implementation types and transition wiring stay private. A small enum switch is a valid simpler alternative for this closed state space; objects are useful when phase behavior grows.

ReportTemplate.render is final (Template Method). It snapshots/validates the entire input list before any hook, then calls heading once, formatLine once for every occurrence in order, and footer once. Each hook must return a non-null single-line String. Its returned text is heading, all formatted lines, then footer, each with a final LF. Empty input still produces heading/footer. Exceptions propagate and stop subsequent hooks; no partial String is returned, and arbitrary hook side effects have no rollback guarantee. Plain/Upper reports supply the hooks. Inheritance couples variants to this skeleton; injected functions can be simpler when only one operation varies.

ReviewQueue owns its mutable membership. add validates a line. Each iterator traverses a snapshot of membership in insertion order with duplicates, independently of other iterators and later additions. String values are immutable and may be shared. hasNext is non-consuming; next advances once and throws NoSuchElementException at exhaustion. remove uses Iterator's default UnsupportedOperationException. forEachRemaining follows this same traversal. The queue is single-threaded and owns no external resource; this exercise promises neither fail-fast live views nor concurrent traversal. List.copyOf(entries).iterator() is a maintainable library alternative to the explicit learning cursor.

Core methods: RuleContext.format; DRAFT.submit, REVIEW.approve and REVIEW.revise inside Document; ReportTemplate.render; ReviewQueue.iterator. Optional ReviewQueue.matching snapshots membership, validates a non-null Predicate first, calls it once per occurrence in order and returns an unmodifiable selected list. Predicate exceptions propagate immediately. A callback's effects remain its own; no rollback is promised.

    javac --release 21 -Xlint:all -Werror -d classes @sources.txt
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

The untouched starter compiles and names its first unfinished method. Complete core and Main work with optional matching unfinished. Main prints:

    PUBLISHED
    Review: Checked
    UPPER
    CHECKED
    DONE
    End
    first
    second

Keep caller-versus-state decision ownership, a transition table, hook call order and two interleaved iterator positions before consulting the reference.

[Iterator contract](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Iterator.html)
[List snapshot contract](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/List.html#copyOf(java.util.Collection))
