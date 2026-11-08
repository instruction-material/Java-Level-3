# Reference decision record
## Observed pressure
Legacy quote and receipt duplicate membership price and shipping decisions. A threshold change risks updating only one endpoint.

## Chosen change and simpler counterfactual
Keep the existing domain records and static API. Centralize price/shipping in quote's two helpers; have receipt render quote's Totals. For this fixed rule set, these ordinary methods are sufficient. A service container and policy hierarchy add wiring without an independent runtime variation requirement.

## Measurement and rollback
The two legacy entrypoints each contain a pricing/delivery decision set; target quote has one centralized set and receipt reuses it. No new domain/interface class is added for core. All606valid combinations are checked for both legacy and target, with exact receipt output and invalid-call cases. A source diff establishes structure; passing outputs establish behavior within the bounded contract. Commit the baseline, extract one helper or redirect one endpoint at a time, run checks after each change, and retain that baseline as the rollback target. A performance improvement is not claimed.

## Optional variation
A requested per-call partner shipping rule creates a real selection seam. quoteWith injects one function without changing quote. It could earn named policy classes if policies later need independent state, configuration or lifecycle. Remove the injection when that independent variation disappears.
