# Collaboration worked example

Java 21. The complete Counter example uses owner-bound opaque snapshots (Memento), one-shot Change objects (Command), registration-based Notices with captured-list dispatch (Observer), ordered request predicates (Chain of Responsibility), a Coordinator owning model/history/notification wiring (Mediator), and Sum/Show operations over a sealed expression family (Visitor).

Counter values and Value leaves are 0..9. Request validates that domain before running gates, stops at the first false, and commits model/history before notifying. Failed notification leaves the committed operation in history; exceptions propagate. A busy guard rejects nested mutations and is always released. Undo is LIFO and returns false when empty. Change's stale guard compares the current value, not revision identity. The integer Memento restores its owner's value only.

Each subscription has independent identity; close is idempotent and affects future captured lists. In-flight snapshot entries still run. Listener exceptions stop later callbacks; reentrant publication has a fresh list. The example is single-threaded and owns no external resource.

Sum uses checked integer addition; Show emits literal parentheses/addition notation. The immutable tree preserves child order; recursive depth is bounded by the exercise. New operations add visitors; new node types require changing all visitors. Simple functions, direct observer calls or switches can be smaller when these separate collaboration boundaries are unnecessary.

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests
    java Main

Main reads no input, writes no files and prints:
    0->2
    true
    false
    2->0
    true
    (2+3)
    5
