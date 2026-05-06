# AJ20 Generic Repository

Build a reusable, type-safe in-memory repository using Java records as immutable data snapshots.

## Requirements
- Define an interface for objects that expose an ID.
- Define at least two Java `record` types that implement the interface.
- Use compact constructors to reject invalid record data.
- Implement a generic repository class with `add`, `get`, `remove`, `contains`, and filtered query operations.
- Prevent duplicate IDs unless the API explicitly supports replacement.
- Return safe views so callers cannot accidentally corrupt internal storage.
- Demonstrate that records provide generated accessors, `toString`, `equals`, and `hashCode`.
- Explain when a normal class is still a better fit than a record.

## Stretch
- Add a sorted query method that accepts a `Comparator`.
