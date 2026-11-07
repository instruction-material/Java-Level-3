# Complete reference

# Creational patterns: report creation

The project compares a constructor, simple static factory, Factory Method, Abstract Factory, and a fluent Builder against explicit contracts. Use Java21 and keep the src tree and sources.txt together.

Report retains the DPJ2 contract: non-null/nonblank/single-line title; non-null list of non-null single-line strings; empty body and blank body lines allowed; CR/LF inside fields refused. Null values throw NullPointerException, other invalid text throws IllegalArgumentException. The list is copied and unmodifiable.

PLAIN renders title + LF, then each body line + LF. BOXED renders "[" + title + "]" + LF, then "| " + bodyLine + " |" + LF. Even an empty body has a heading with its final LF. These are the exercise's plain and boxed text layouts, not an external document standard.

RendererFactory.forStyle selects a fixed enum choice through a static method. Exporters.Exporter has the Factory Method hook: it validates a report, calls createRenderer once, rejects null products/results, and delegates rendering. Subclasses choose one renderer; a creation failure propagates. No product freshness guarantee is inferred from the pattern.

Families.Factory creates two different product types, Heading and BodyLine. Products have stable non-null Style metadata. FamilyRenderer accepts matching metadata and rejects a mixed pair with IllegalArgumentException. Null families/products/styles/text/reports are rejected with NullPointerException.

ReportJob snapshots report, style, includeCount and footer. Builder defaults: PLAIN, no count, empty footer. Setters return the same builder; style/footer reject null. A footer may be empty or single-line; CR/LF is invalid. build returns a new immutable value, so later builder changes leave older jobs unchanged. A direct ReportJob constructor remains a valid simpler alternative for a small fixed configuration. Count output is "Lines: N" + LF after the report. A nonempty footer follows with LF; count/footer text is not boxed.

Core tasks: complete BoxedFactory's two products, Boxed Exporter's creation hook, and the four builder methods. Optional toBuilder returns a fresh builder carrying every original option; changing it preserves the original job. Core commands must work while this optional method is unfinished.

    javac --release 21 -Xlint:all -Werror -d classes "@sources.txt"
    java -cp classes ContractTests factories
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

The final command also checks the optional copy. Main prints:
    [Notes]
    | one |
    | two |
    Lines: 2
    End

The untouched starter compiles, then reports the named unfinished method reached by the harness. Keep each Main project in its own folder. These programs read no standard input and create no output file. Preserve a decision table, the learner attempt, a compatible/incompatible product case, and a builder-state trace before consulting the reference.
