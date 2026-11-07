# Behavioral choices worked example

Java21. This complete example uses a caller-selected LabelStyle (Strategy), a Gate with private CLOSED/OPEN/LOCKED state objects (State), a final ListPrinter.print algorithm with an item hook (Template Method), and an immutable Labels snapshot using Java's existing Iterator.

Gate opens only from CLOSED and locks only from OPEN. Other transitions throw IllegalStateException and preserve state. Separate gates have independent state; shared enum constants contain no mutable gate data. Small closed transitions could use a plain enum switch instead.

styled checks a non-null style/input/result and calls the selected style once. ListPrinter snapshots and rejects null input/elements before hooks, calls item once per occurrence in order, and rejects a null result. Its literal bracket/pipe notation has no escaping convention. Exceptions propagate and stop later calls. Custom hooks may have external effects; the returned String does not promise rollback.

Labels snapshots list membership, retains immutable String values, preserves duplicates/order and exposes an unmodifiable list. Its independent iterators use Java's standard exhaustion/removal contracts. The example uses one thread and no external resources.

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

Main reads no input and prints LOCKED, READY, [-one|-two], a and b on separate lines. It writes no files. A direct function, enum switch or ordinary list iteration can be a smaller solution when no separate behavioral boundary is needed.
