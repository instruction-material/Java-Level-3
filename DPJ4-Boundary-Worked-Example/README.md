# Boundary patterns: complete worked example

Java 21. These three files are complete public teaching code, separate from the quote assignment. Draft.copy owns a fresh mutable list. String elements are immutable, so copying the list is sufficient here; a mutable element type would need its own copy policy. snapshot returns a validated immutable Note, and later draft edits preserve older snapshots.

Legacy exposes pipe-delimited text. The first field is a nonblank title and later fields are body lines, including an empty trailing field. Literal pipes are delimiters; this tiny format supplies no escaping convention. No field may contain CR/LF. Adapter calls fetchPipeText once, rejects null and invalid data, and implements the typed NoteSource contract. Facade loads once and renders the title/body with LF and a final LF. Source failures propagate; there is no retry or output effect. The sample Legacy lambda has no network or file dependency.

LineEnding.INSTANCE is an immutable enum Singleton example, one enum value per defining class loader. Adapter and Facade receive dependencies explicitly; unrelated consumers can use different NoteSource instances without changing global state. The mutable Draft is used within one thread. Pattern selection should follow the actual copying, compatibility or subsystem-access pressure; direct Note construction is sufficient for a fixed in-memory value.

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

Main reads no input and creates no output file. It prints 1, 2, T, Ready and Done on separate lines.
