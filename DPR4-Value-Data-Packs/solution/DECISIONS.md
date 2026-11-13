# Representation decision record

- Baseline invariants and exact transport example:
- Why ItemCode is a value rather than an entity:
- What RequestLine groups and validates:
- Why destination remains a String under this contract:
- Why ServiceLevel is a finite enum rather than a subtype hierarchy:
- Which collections/arrays are copied; which immutable elements may be shared:
- Equality/order/duplicate policy and a map-key observation:
- Codec boundary, rejected row, and preserved round-trip bytes:
- ClientCompatibility and baseline hashes retained:
- Optional merge behavior and separate verification:

Reference decisions: ItemCode owns one exact immutable code invariant. RequestLine binds a code to a checked quantity. Destination needs only one boundary rule and stays a String. ServiceLevel is closed and owns a single dispatch rule; there is no independent state machine. SupplyRequest snapshots the input List and its records contain immutable components. Array getters construct new arrays. Lists preserve order and duplicate lines, so regrouping is an explicit optional feature. RequestCodec alone interprets the old delimiter shape. Constructor argument evaluation validates destination before service and array entries, preserving the existing error order. Stable ordered values preserve map lookup after caller mutation.
