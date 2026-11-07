# Learner starter

# Java design foundations

This Java 21 pack supplies the before-refactor ReportBlob and a package-based report publisher. The core has three learner tasks: Report validation/snapshot ownership, TextFormatter, and ReportPublisher.publish. UppercaseTitleFormatter is one optional extension. Main reads no standard input and creates no output file.

## Contract

Report has a non-null, nonblank, single-line title and non-null list of non-null, single-line body strings. Blank body lines and an empty body are allowed. Embedded CR or LF characters are invalid. Null values throw NullPointerException; other invalid content throws IllegalArgumentException. Preserve title/body text exactly. Snapshot the input list and expose an unmodifiable list; String elements are immutable.

TextFormatter returns title + LF followed by each body line + LF, including a final LF. Empty body therefore produces only title + LF. publish rejects null input, calls the injected formatter once, rejects a null formatted result, then calls the injected sink once. Formatter failure prevents a sink call. Sink failure propagates; there is no retry or rollback guarantee. A ReportSink may have external effects; the supplied tests use a recorder without network, disk or clock access. Constructors reject null collaborators.

The optional UppercaseTitleFormatter changes only the title with Locale.ROOT, retaining body text and the LF contract. It works through the existing ReportFormatter interface. A record does not recursively freeze a mutable list; List.copyOf plus immutable String elements establishes this report's ownership contract.

LanguageChoices is a complete worked example. PreviewMode is a small fixed enum; its sealed Outcome family contains Published and Refused records. Preview requires a nonempty body, otherwise Refused("No body"). TITLE_ONLY returns the title + LF without calling the formatter; FULL uses the formatter. All preview parameters and the formatter result are non-null. lambdaExample demonstrates a one-method formatter without a named implementation class.

## Packages and execution

domain uses Java values only. ports declares application contracts and may refer to domain. application imports domain and ports. adapters implement ports. Main selects concrete implementations at the composition boundary. legacy is the supplied before-refactor example; application does not import it. Records/final classes have closed inheritance where it is useful; formatter/sink interfaces remain open for real adapters and test doubles.

Run in the extracted project root, keeping sources.txt and the src tree intact:

    javac --release 21 -Xlint:all -Werror -d classes @sources.txt
    java -cp classes ContractTests core
    java -cp classes Main
    java -cp classes ContractTests

Main prints Weekly report, Ready, Checked on separate lines. The untouched starter compiles and rejects unfinished core work. Complete the three core tasks before expecting core checks and Main to pass. The optional fourth method is required only by the complete harness; it must not block a finished core project.

The reference includes both core and extension implementations. Keep source roles and earlier work separate. A passing harness establishes the documented behavior; retain the before/after responsibility map, package dependency arrows and one explanation of why each interface creates a useful test seam.
