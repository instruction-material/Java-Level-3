# Clinic 14 review record

## Before changing source
Public call and exact input/output:
Source locations and concrete smell:
Behavior, validation order and data that must stay stable:
Chosen boundary and why it fits the problem:

## Actual small steps
Retain each source checkpoint or commit identity.
For each step: prediction, change, passing output and next risk.
First extraction:
Next responsibility move:
Final client and boundary checks:
Recoverable checkpoint or postponed change:

## Review
Before/after responsibility sketch:
One observed benefit and one tradeoff:
Standard, boundary and failure cases:
One tempting change that would alter behavior:
Pattern or technique justified by this case:

## Optional independent work
Transfer predictions, actual output and retained inputs:
Extension predictions, actual output and retained inputs:
What these observations establish and what needs further review:

## Reference notes

## Split a reused temporary before extracting queries

The working measure(watts,seconds,efficiency) reuses a temporary first for raw energy and then for usable energy. Its input is constant power in watts, a duration in seconds, and an integer efficiency percentage. This simplified calculation uses raw=watts*seconds joules, usable=floor(raw*efficiency/100), and lost=raw-usable. It models the stated arithmetic, not a physical measurement or variable-power simulation.

Validation order is watts (0..1000), seconds (0..3600), then efficiency (0..100), inclusive. Invalid values throw IllegalArgumentException with message "watts", "seconds" or "efficiency". Each EnergyResult is immutable; raw is 0..3600000, usable is 0..raw and lost must equal raw-usable. Its constructor validates those fields in that order, reporting "raw", "usable" or "lost".

For (33,3,50), raw is 99, usable is 49 and lost is 50. Multiplying before integer division is essential: 99/100*50 gives the wrong usable value. For (1000,3600,100), raw and usable are 3600000 and lost is zero. Zero power, duration or efficiency follows the same formula.

First name rawJoules and usableJoules separately while keeping all arithmetic and validation order. Run baseline and record those two values. Then complete the public raw(watts,seconds) and usable(raw,efficiency) query scaffolds and call them from measure. raw validates watts then seconds and returns their product. usable validates raw in 0..3600000 with IllegalArgumentException("raw"), then efficiency, and returns raw*efficiency/100. The stated bounds keep this product within int.

The reference gives each query one meaning and no state changes. Query extraction is appropriate because the computation is repeatable and has no outside effects. A temporary that holds a one-time random, file or network result cannot simply be replaced by a repeatedly executed query.

Record the reused variable's meanings, the split-variable checkpoint, exact intermediate values, the extracted query boundaries and the rounding-sensitive counterexample. Retain an invalid input and a maximum input. Explain why changing rounding policy would be new behavior.

This final reference illustrates the described responsibility split. Retain the actual source checkpoints used in an attempt; passing behavior alone does not establish that sequence or its design judgment.
