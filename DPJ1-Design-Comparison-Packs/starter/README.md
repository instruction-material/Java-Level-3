# Extension starter

# Design comparison and overnight extension

Compare a plain function, four inherited order combinations, and Checkout composed from independent pricing/shipping policies. Two clients, displayQuote and invoiceTotal, use the same Checkout contract.

Input: integer units 0..1,000,000. Gross cents per unit: 350 below 5 units, 300 from 5 through 9, 250 from 10. Membership removes one tenth of the subtotal. Pickup costs zero; courier costs 500 + 20 * units cents; overnight costs 1500 + 30 * units cents. Every shipping policy charges zero for zero units. Invalid quantities throw IllegalArgumentException; null policies or a null shipping enum throw NullPointerException. Arithmetic stays within int for this domain.

For six units: ordinary subtotal 1800, membership subtotal 1620, courier fee 620, overnight fee 1680. Main prints 1800, 2420, 1620, 2240 on separate lines. Overnight totals are 3480 (regular) and 3300 (member).

The starter has complete baseline behavior. Implement only OvernightShipping.feeCents for the extension, keeping Checkout and its clients compatible. The plain function already provides the overnight rule for comparison. The four legacy classes cover pickup/courier; a third mode would need two more combined classes, while composition adds one shipping policy. A small fixed program may reasonably keep the plain function; interfaces add indirection and should match a variation requirement.

Classification examples are included: simpleMinutes(2) and factoryMinutes(2) both give 120; the latter adds types without a current alternative policy. inconsistentInvoiceCents(5) gives 1750 while the stated bulk rule gives 1500: a divergent duplicated business rule.

Use Java 21 in this role directory:

    javac --release 21 -Xlint:all -Werror Main.java ContractTests.java
    java ContractTests base
    java ContractTests
    java Main

Base tests pass in both roles. Full tests reject the untouched extension starter with UnsupportedOperationException. The reference passes the baseline and overnight contracts. Compile separately from other Main projects. A passing harness verifies behavior; the design decision and explanation are separate completion evidence.
