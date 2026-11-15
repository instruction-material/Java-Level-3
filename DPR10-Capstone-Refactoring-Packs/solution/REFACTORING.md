# Capstone record

## Application and diagnosis
Entry points and reproducible commands:
Smells with source locations and consequences:
Responsibilities and state that belong together:
Behavior promises and characterization examples:

## Planned sequence and risks
For each step: technique, protected behavior, likely regression, check and rollback point.
First small extraction:
State/behavior move:
Reporting boundary:
Postponed behavior change and reason:

## Actual steps
For each step: prediction, changed files, passing result and what was learned.
Retain the intermediate sources or commit identities.

## Architecture and pattern decisions
Before/after responsibility sketch:
A change now confined to fewer files:
Remaining coupling and next justified improvement:
Patterns that emerged:
Patterns considered and left unnecessary:

## Reflection
Most useful technique and evidence:
FIFO counterexample to a tempting change:
What automated checks establish and what still needs review:

## Optional independent work
Trace comparison: matching case, mismatches and observations.
Rename preview: position, retained history, collision and closed failure.

## Reference design notes

The public WorkshopDesk owns the ordered session catalog and validates API inputs. SessionBook owns one session's capacity, open state, enrolled set, FIFO waiting list and history. DeskReports receives snapshots; reporting cannot mutate live books. The legacy before design keeps five coordinated maps and rendering together.

A small intermediate step can extract the enrolled-or-waiting membership predicate first. Moving session state and its operations together then reduces the catalog's coordination risk. Rendering can be extracted after snapshot behavior is characterized. Each step must retain the exact API, errors, histories and reports.

This final source demonstrates the proposed responsibility split. It does not supply a learner's sequence of intermediate commits. Retain actual small changes and their check results in a personal record.

Sorting waiters was postponed because it changes FIFO behavior; cy joins before bo and must be promoted first. Persistence and a live rename command are also separate features. The optional preview demonstrates that separation without changing the live desk API.

WorkshopDesk is a facade over per-session objects. A report helper is sufficient for the two fixed formats. A future need for caller-supplied rendering policies could justify Strategy. Open/closed checks alone do not justify a State hierarchy.

The trace comparator observes return values, exception class/message and snapshots after every step. The rename preview changes only a copied snapshot and appends an event while preserving original history. Both optional tasks run with the before-design core. Automated checks support bounded behavior claims; the risk map, intermediate changes and architecture review support the design argument.
