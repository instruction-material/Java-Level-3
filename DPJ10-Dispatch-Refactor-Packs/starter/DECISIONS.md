# Dispatch desk refactor record

## Baseline and contract
Record the source/check identity, command and outputs before any structural change.
Define request validation, preview purity, duplicate-id rejection, snapshot ownership and commit/notification order.

## Three to five observed pressures
For each, cite concrete source locations and the change scenario that hurts.

## Decisions and simpler counterfactual
For each chosen boundary/pattern, give the desired property, simpler alternative, added types/indirection, wiring/test cost and removal trigger.
A passing unchanged starter is the starting point. Source review establishes structural improvement.

## Small steps and rollback
Retain one behavior-preserving change per commit after export, matching checks and recoverable source identities.
Separate intentional contract changes from refactoring.

## Measurements and demonstration
Define rule-location, files-touched and dependency counts; count retained LegacyDesk separately.
Show a substituted renderer/store/sink in a controlled test and explain the failure boundary.
Name one pattern deliberately not used and one remaining tradeoff.

## Optional batch
Record copied input ownership, prefix commits, notification failure and unchanged core acceptance.
