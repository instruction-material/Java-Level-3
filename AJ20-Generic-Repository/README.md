# AJ20 Generic Repository

Build a reusable, type-safe in-memory repository.

## Requirements
- Define an interface for records that expose an ID.
- Implement a generic repository class with `add`, `get`, `remove`, `contains`, and filtered query operations.
- Prevent duplicate IDs unless the API explicitly supports replacement.
- Return safe views so callers cannot accidentally corrupt internal storage.
- Demonstrate the repository with at least two record types.

## Stretch
- Add a sorted query method that accepts a `Comparator`.

