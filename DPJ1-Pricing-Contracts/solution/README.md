# Complete reference

# Bulk pricing contract

Implement PricingStrategy.priceFor(int units) in BulkPricingStrategy.java. Keep the interface and Main caller compatible.

Accept integer quantities 0..1,000,000 inclusive. Every unit receives the rate selected by the whole order quantity: 0..4 units cost 3.50 each; 5..9 cost 3.00 each; 10 or more cost 2.50 each. This is whole-order pricing, not marginal tiers. Zero items cost zero. Reject other integers with IllegalArgumentException.

Examples: 4 -> 14.0; 5 -> 15.0; 6 -> 18.0; 9 -> 27.0; 10 -> 25.0; 11 -> 27.5. The total drops from 27.0 to 25.0 at the ten-unit boundary. The rates are exactly representable halves; the public double return type is preserved.

Use Java 21 from this directory:

    javac --release 21 -Xlint:all -Werror Main.java PricingStrategy.java BulkPricingStrategy.java ContractTests.java
    java ContractTests
    java Main

Compile this role separately from other Main classes. The untouched starter compiles, then throws UnsupportedOperationException at its unfinished method. A completed implementation prints "Pricing contracts: 10014 passed"; the demo prints 18.0.

The interface is retained for the exercise's API. One fixed rule in one caller could also be a plain method. Explain which actual variation requirement would justify interchangeable implementations before adding abstractions.
