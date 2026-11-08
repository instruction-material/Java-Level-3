# Reference dispatch desk decision record

## Pressure and preserved behavior
preview and send duplicate channel/priority formatting and rate decisions. Desk also owns map details and effect ordering, so a storage substitute or computation-failure test reaches into the same class. Preserve the public DeskApi, bounded request domain, exact rendered text/cents, preview purity, duplicate checks, immutable insertion-order snapshots and commit-before-notice failure boundary.

## Chosen boundaries and counterfactual
A shared private computation helper is the smallest remedy for duplication. CHECKPOINTS.md demonstrates that runnable intermediate step. The reference additionally supports substituting rendering and storage independently in controlled tests: Renderer owns computation, DispatchRepository owns insertion/snapshots, and Desk owns the use case and reentrant guard. Composition selects adapters. NoticeSink remains the existing synchronous notification port.

Three concrete pressures justify these choices: repeated rule changes in two entrypoints; storage replacement coupled to the service; and isolated rendering/save/notice failure tests. A private helper alone solves the first pressure and costs less. The other two provide this example's reason to add seams. Domain values and the stable two-channel switch stay plain.

## Measurements with explicit scope
Count only target code, with unchanged LegacyDesk reported separately.
- Two target entrypoints contain rate/rendering rules initially; one StandardRenderer.render owns them after refactoring.
- A price change touches one target Java file before and after. File count alone shows no improvement; the number of rule locations does.
- Count production Java files including Main and Composition but excluding *Tests.java: eight initially, twelve in the reference. Four added types are two ports and two adapters.
- Desk's constructor receives one collaborator initially and three afterward. This adds wiring and navigation.
- The reference app/domain/ports slice compiles without infrastructure or LegacyDesk. Separate controlled tests replace rendering/store/notification and observe failures.

These are structural counts, not measured development-time savings. Retaining the baseline intentionally retains its duplicate rules.

## Failure and effect ownership
send rejects an existing id before rendering. A rendering failure precedes insertion. A successful insert precedes notification. Notice failure leaves the inserted outcome and propagates; retry is a duplicate. The guard releases in finally. A trusted storage adapter that throws after its own mutation can have partial effects; no cross-adapter transaction exists.

## Decisions omitted and removal triggers
No factory hierarchy: the closed two-channel switch is small and explicit. No event bus: only one synchronous sink is required. No database, async queue or container is added. Renderer/repository seams can be removed if independent substitution and isolated tests cease to be valuable and a direct implementation better serves the actual application.

## Change and rollback evidence
Retain original source/check output before each learner change. First centralize computation, then move storage, then inject rendering and wire composition, rerunning checks after each recoverable commit. The native verifier executes the initial roles, the computation-only intermediate and the complete reference; it does not infer a learner's commit history or design reasoning.

## Optional prefix batch
Capture a defensive copy before callbacks. Delegate items to send and return immutable results. A later failure preserves earlier commits; notification failure also preserves the failing item's insert. Empty nested batches are rejected while a send is active. Core remains independent of this extension.
