# Concept sources

These original teaching examples use the following terminology. Retrieved 2026-11-15.

- https://refactoring.guru/replace-constructor-with-factory-method : introduce a named/static construction function before changing construction internals.
- https://refactoring.guru/design-patterns/factory-method : a creator's overridable construction method supplies a common product to its algorithm.
- https://refactoring.guru/design-patterns/strategy : externally selected interchangeable algorithms.
- https://refactoring.guru/design-patterns/state : behavior changes with the context's current state.
- https://refactoring.guru/design-patterns/template-method : a shared algorithm with selected subclass hooks.
- https://refactoring.guru/design-patterns/adapter : a boundary conforms to the client-required interface.
- https://refactoring.guru/introduce-local-extension : add operations around an unmodifiable type.
- https://refactoring.guru/design-patterns/facade : a simple client interface for a subsystem.
- https://refactoring.guru/design-patterns/command and https://refactoring.guru/design-patterns/memento : encapsulated requests and opaque saved state are distinct roles.

A static factory alone is not the subclass-based Factory Method pattern. The code intentionally retains a small style-selection switch and compares guard-only, Strategy and State solutions; it does not equate every conditional with a missing pattern.
