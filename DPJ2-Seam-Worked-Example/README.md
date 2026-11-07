# Worked constructor-injection example

This complete teaching example is separate from the unfinished report project. Message wraps one non-null immutable String; Sender receives a non-null Sink through its constructor. send rejects a null Message before writing, writes its exact text once, and propagates a sink failure. Empty strings, line breaks and Unicode text are allowed here; the later Report project adds its own stricter line/list contract.

The demonstration injects StringBuilder.append as an in-memory destination through the single-method Sink interface. No network, file or clock is used. Main prints Ready followed by LF.

Use Java 21 in this example's own directory:

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

The complete harness reports Seam worked contracts: 14 passed. Edit the text in Main before rerunning; keep this project's Main separate from the report project's Main. This example has no learner TODOs.
