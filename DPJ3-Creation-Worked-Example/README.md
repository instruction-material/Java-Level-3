# Creation choices: complete worked example

Java 21. Main.java and ContractTests.java are complete public teaching code. Label's constructor and named static factory both produce a value. supplied accepts a Supplier, calls get once, rejects null, and permits a cached Label. Brackets chooses a Token in the inherited Creator.display workflow through the create hook (Factory Method). Theme supplies two related product types, Heading and Body (Abstract Factory). PageBuilder names two optional settings and builds immutable Page snapshots. A direct Page constructor produces the same value with less machinery for a fixed configuration. This fluent configuration builder is a common Java Builder variation; no separate director is required here.

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

Main reads no input and creates no output file. It prints:
    Ready
    Ready
    Ready
    [Ready]
    **Title**
    > Body
    [Title]
    Body

Page values are immutable because their components are immutable Strings and a boolean. The mutable builder is not shared between threads. Interfaces alone cannot enforce compatible family semantics; each Theme implementation is responsible for its pair. Compare which point varies and which extra classes each choice requires before selecting it.

Supplier contract: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Supplier.html
