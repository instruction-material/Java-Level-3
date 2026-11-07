# Prototype, adapter, facade and dependency lifetime

Java 21. Keep all ten files and src/boundary together. This project retains the existing bulk-pricing context and adds the actual copy/translation/subsystem boundaries.

OrderDraft takes a deep copy of its input list and mutable Line elements. Its lines list is structurally read-only, but a Line remains editable. A prototype copy must own fresh Line elements. Line units are 1..1000; a draft can be empty. Titles/names are non-null, nonblank single lines. Null causes NullPointerException; other invalid values cause IllegalArgumentException. totalUnits uses checked integer addition.

LegacyPrice.Standard accepts a decimal integer string for 0..1000 units and returns "USD " plus an exact two-decimal total. Unit cents are 350 below 5 units, 300 for 5..9, and 250 from 10. PriceAdapter implements the typed PricePort: validate quantity before calling legacy once, convert the integer to text, reject null or a response outside USD [digits].[two digits], and return BigDecimal with scale exactly two. Legacy failures propagate. No floating-point money conversion is used.

QuoteFacade snapshots the draft, validates aggregate quantity <=1000 before pricing, calls its injected PricePort once, and returns an immutable Quote. A price is non-null, nonnegative, and scale exactly two. It reads/writes no files. No retry is promised. Facade coordinates these steps; Adapter translates one foreign API. Neither requires hidden global state.

CurrencySettings.INSTANCE is an immutable enum Singleton example, one enum instance in a defining class loader. Multiple class loaders/processes can have separate instances. Counter is explicitly constructed and mutable; two scopes start independently. The facade's constructor receives its pricing dependency, allowing independent fake prices in tests. Single-instance lifetime alone does not make mutable collaborators safe for concurrent access.

Core: implement OrderDraft.copy, PriceAdapter.total, and QuoteFacade.quote. Optional: implement copyWithTitle as an independent deep copy with a new valid title. Completed core and Main remain runnable with the optional method unfinished.

    javac --release 21 -Xlint:all -Werror -d classes @sources.txt
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

Main reads no input and prints "Class order: 6 units = USD 18.00" followed by LF. The untouched starter compiles and names the first unfinished method. Keep a mutable-object ownership sketch, an adapter request/response table, a facade call trace, and an explicit dependency-lifetime decision before comparing the reference.
