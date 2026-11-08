# Java-Level-3

Canonical repository for the advanced Java course family used by the live
classes catalog.

This repo intentionally backs these public courses:

- Java Level 3
- Design Patterns in Java
- Design Patterns in Java Part 2: Refactoring

The old standalone local folders for the two design-patterns courses were exact
duplicates and are not canonical sources. Keep new Java design-patterns and
refactoring material here unless the public catalog is changed to point at a
separate published repo.

## Dispatch desk capstone source

`DPJ10-Dispatch-Refactor-Packs` contains a fully runnable starter, preserved legacy baseline, reference architecture and optional prefix-commit batching. Each role's README and CHECKPOINTS.md define behavior, build commands and review evidence. Java 21 verification runs with `python3 tools/verify-capstone.py`. This source preparation precedes matching catalog/IDE acceptance.


## Applied Strategy studio

`DPJ-Studio-Strategy-Selection-Packs` provides a working pricing baseline,
a Strategy reference, a helper-only counterfactual and independent Java 21
contracts. The optional injected-rule method is separate from the core refactor.
Each role includes its exact build list, supplied cases and decision record.
Verify with `python3 tools/verify-studios.py`.

The Structural Wrapper studio in `DPJ-Studio-Structural-Wrapper-Packs` supplies
a separate text-source case with ordered counting/cache/transform wrappers,
independent lifecycle checks and optional selective invalidation.

The Architecture Judgment studio uses a versioned notebook to compare a helper-only cleanup with an injected repository. Its independent contracts preserve stale-edit, snapshot and audit-failure behavior; its application compiles without the memory implementation.
