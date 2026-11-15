# Clinic 11 record

## Trace and diagnosis
Original public call and exact input:
Data read from another object:
Decision and proposed owner:
Why this is feature envy rather than useful coordination:

## Safe steps
For each step retain prediction, source change, check result and rollback point.
Extract a leg method:
Move route state and distance:
Move summary and review public calls:

## Boundaries and evidence
Null, empty, repeated and unusual-coordinate results:
External client result:
Input snapshot retained:
Before/after responsibility sketch:
Benefit and construction tradeoff:
Postponed change:

## Optional work
Core continuation: empty/unusual repeated client and ownership comparison.
Transfer: longest differs from total and query purity.
Extension: exact index, retained input and ordered failures.

## Reference design notes

The original TripPlanner knows the route's list representation and performs decisions over that foreign state. RoutePlan now privately owns the name and immutable points and provides distance/summary itself. TripPlanner constructs it at the facade boundary; the frozen input value and existing public client remain unchanged.

Extracting a leg method is a small first move. Moving state and distance together establishes the ownership boundary, after which summary can follow. This reference is a final design rather than a learner's intermediate history. Retain the actual steps used to reach it.

Empty routes produce zero instead of requiring a dummy point. Null input remains an explicit failure. Opposing coordinate differences use the sum of absolute differences; taking the absolute value after adding would change behavior.

The longest-leg transfer uses the maximum of adjacent distances rather than the sum. The replacement preview copies one point position and creates a new immutable input value; it adds behavior separately. A closed tour, real geographic distance or mutable route editing would be future feature work.
