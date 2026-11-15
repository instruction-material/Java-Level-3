# Concept sources

Original journal and test fixtures authored for the testability/refactoring topic. Existing pricing examples remain in their historical folders.

- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Clock.html : substitute a clock for deterministic time-dependent tests.
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/IntSupplier.html : inject a primitive-producing function without a framework.
- https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/UncheckedIOException.html : retain an I/O cause at an unchecked boundary.
- https://martinfowler.com/articles/injection.html : separate dependency assembly from its use.

Characterization records observed behavior before changing structure. Deterministic fakes make specified values and dependency calls repeatable. A normalized approval masks only declared volatile fields; it remains evidence for the preserved fields, not proof about all nondeterministic behavior.
