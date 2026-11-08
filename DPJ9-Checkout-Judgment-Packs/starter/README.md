# Checkout refactoring judgment
Use Java 21. Preserve LegacyCheckout, Order, Totals and ContractTests as the behavioral baseline.
The program uses only bounded integers, reads no input and writes no files.

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes Main base
java -cp classes ContractTests core
java -cp classes Main
```
Baseline runs before either core method is complete. The untouched starter compiles and explicitly rejects its first unfinished quote. Complete Checkout.quote and receipt, preserving the public behavior. The reference centralizes stable rules in ordinary helper methods; a larger pattern hierarchy requires a concrete change pressure and cost argument. The helper skeleton supplies one possible route rather than a requirement to preserve that internal structure.

Order has 0..100 units, REGULAR/MEMBER tier and PICKUP/STANDARD/EXPRESS delivery, all non-null. Prices are integer cents: regular 200/unit, member 180/unit. Zero units and pickup have zero shipping. Positive-unit express always costs 900. Standard costs 500 unless subtotal >= 2000, when it is free. Membership affects subtotal, so it can change eligibility for free standard delivery. Totals are immutable nonnegative values; total uses checked int addition. Receipt is the exact six-field pipe-separated tier|delivery|units|subtotal|shipping|total string. Missing order is rejected with NullPointerException.

Main without arguments prints:
```text
REGULAR|STANDARD|10|2000|0|2000
REGULAR|STANDARD|10|2000|0|2000
2700
```
Main base prints the first receipt and 2700. Both legacy entrypoints are checked against an independently written rule table over all 606 allowed combinations. Core also checks both target entrypoints. Behavioral tests do not prove design quality, removal of duplication or a justified pattern choice. Keep your decision and measurements in DECISIONS.md.

Optional quoteWith receives one trusted ToIntFunction<Order> shipping policy, called exactly once, even for zero units. It replaces the normal shipping calculation for this call only. Return 0..10000 cents; null input/policy, invalid fee and callback exceptions fail. Baseline quote behavior stays unchanged. This extension has an actual variation seam; an injected function is enough before separate policy classes earn their cost. Run the full ContractTests after core to verify it.
