# Refactoring record

Record one step per entry: change pressure, predicted exact output, file changes, command and actual result. Keep the frozen API, Legacy implementations, AncientDay, CompatibilityClient and ContractChecks unchanged.

## Required notice journey

1. Record both formats and both orderings; retain the original input list.
2. Extract a row-formatting method without changing its owning class. Run baseline checks.
3. Move formatting to NoticeLine products, then extract creator collaborators. Run baseline checks.
4. Introduce the static forStyle selection factory; migrate construction calls. Identify what this step accomplishes by itself.
5. Let TextCreator and BracketCreator override createLine while NoticeCreator owns the common algorithm. Run core checks.
6. Extract the interchangeable Ordering algorithm. Compare a one-flag design with a third caller-supplied ordering. Repeat checks with assertions disabled.
7. Record which intermediate step supplied most of the benefit and when the full design should be simplified again.

## Optional state journey

Record the full state/action table, a rejected action, a complete cycle, an earlier immutable history and invalid-action behavior. Compare GuardTicket, StrategyTicket.standard and the refactored Ticket. Choose a new requirement that favors each design; preserve standard behavior while measuring the changed files. Keep this completion independent of core and editor completion.

## Optional editor journey

Extract date conversion, introduce an opaque snapshot, then move requests to commands and their two stacks to history. Keep Editor as the client facade. Record append/undo/redo, invalid append after undo, and branch replacement that clears redo. Compare the wrapper extension with an adapter that directly wraps AncientDay. Keep this completion independent of the other projects.
