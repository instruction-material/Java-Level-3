# Structural patterns: complete worked example

Java 21. These three files are complete public teaching code. Message subclasses and TextStyle implementations vary independently (Bridge). Sequence snapshots an ordered child list and treats nodes uniformly (Composite). Emphasis adds visible behavior around one Node (Decorator). LazyNode defers creation, caches a successfully loaded Node and delegates every render (Proxy). WordPool shares immutable Word values for two Lexeme keys while At owns a row separately (Flyweight).

String output is the defined observation. A sequence uses brackets and commas, emphasis uses asterisks, Plain preserves text, and Upper uses Locale.ROOT. These programs read no input and create no output file. Main prints:
    [Ready,*WARNING: WAIT*,GO]
    ready@1
    ready@2
    true

Sequence owns an unmodifiable child-list snapshot and retains child identities. Supplied nodes can still be mutable; identity is distinct from immutability. Graphs are bounded/acyclic. LazyNode is single-threaded; failed/null loading propagates and leaves it unloaded, while a successful product stays cached even if a delegated render later fails. Each separate render can attempt loading again after an unsuccessful load. No automatic retry, resource ownership or disposal operation is added.

Word values are immutable and shared only within one pool; separate pools can hold equal but distinct records. At's row is0..9 and is excluded from the pool key. The two-key pool demonstrates intrinsic/extrinsic separation, not a measured heap-saving result. A fixed two-value application may use its enum directly; a pool adds map/allocation/navigation costs and needs a concrete reuse pressure.

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

Keep this Main separate from the scene assignment. Compare which responsibility and lifetime each boundary changes before selecting it.
