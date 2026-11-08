# Strategy Selection Refactor Studio

The working starter calculates all core prices correctly. Its quote and receipt
methods repeat pricing decisions. The refactor preserves observable behavior,
centralizes each pricing rule and compares a Strategy design with a smaller
shared-helper design. The optional injected-rule method starts unfinished.

## Supplied contract

Quantities are whole integers from 0 through 1000. Prices are whole cents.
Plans are BULK, FLAT and MEMBER.

| Plan | Unit price or rule |
| --- | --- |
| BULK, 0–4 units | 350 cents per unit |
| BULK, 5–9 units | 300 cents per unit |
| BULK, 10–1000 units | 250 cents per unit |
| FLAT | 400 cents per unit |
| MEMBER | 90% of the BULK total, using integer arithmetic |

`CartPricer.quote(plan, units)` returns the total. `receipt` returns exactly
`PLAN|units|total`. Null plan raises NullPointerException before quantity
validation. An out-of-range quantity raises IllegalArgumentException.
The same inputs can be reused; these operations have no external effects.
PricingRule implementations receive an already validated quantity.

For six units, BULK = 1800, FLAT = 2400 and MEMBER = 1620 cents.
Bulk boundaries are 4 → 1400, 5 → 1500, 9 → 2700 and 10 → 2500.
Zero costs zero for every built-in plan.

## Run the baseline and current project

Use JDK 21 in a clean project folder:

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes Main base
java -cp classes Main
```

Both Main commands print:

```text
1800
2400
1620
BULK|6|1800
```

The baseline and core modes pass before edits. `src/pricing/legacy/LegacyPricer.java`
is the preserved baseline. `ContractTests.java` contains an independent price-band
oracle for every plan and every valid quantity, plus invalid and boundary cases.

## Core refactor

1. Run the baseline and core commands; record their exact outputs in DECISIONS.md.
2. Trace how quote and receipt each choose and calculate the six-unit BULK price.
   Change the BULK boundary on paper and identify both places affected.
3. First try a helper-only change: let receipt call quote. Preserve its output,
   exceptions and validation order, then run core again.
4. Compare that intermediate with the reference's PricingRule implementations.
   State the additional variation that justifies three rule classes and the
   selection map. Keep public quote and receipt signatures unchanged.
5. Centralize rule ownership, update sources.txt for any added Java files, and
   run the core checks after each coherent change.
6. Compare the target computation owners (two to one), source files and setup
   cost. Count the preserved legacy copy separately from the edited application.

The reference separates BULK, FLAT and MEMBER calculations. MEMBER delegates the
bulk portion rather than copying its bands. CartPricer selects a rule once and
receipt reuses quote. A helper-only design remains a valid counterfactual;
passing behavior tests alone does not establish a useful Strategy refactor.
Review the actual structure and the decision record as well.

## Optional transfer and extension

Written transfer: a one-day rule charges 275 cents per unit with a 500-cent
minimum for positive orders; zero remains zero. Expected totals for quantities
0, 1, 2 and 6 are 0, 500, 550 and 1650. Compare a lambda with a stateful rule
class and explain whether the existing public plan selection needs changing.

Code extension: implement `priceUsing(units, rule)`. Validate quantity first,
require a non-null rule, call it exactly once with the original quantity and
return its nonnegative long result unchanged. Negative results raise
IllegalArgumentException. A rule's exception propagates unchanged, and ordinary
quotes still work afterward. This is an in-process calculation boundary.

Run the complete extension checks with:

```sh
java -cp classes ContractTests
```

The starter deliberately raises UnsupportedOperationException here until the
optional method is implemented. Core checks remain independent of that method.

Review the extension separately: standard custom rule, zero, quantity limits,
null rule, negative result, maximum long result and thrown exception.
