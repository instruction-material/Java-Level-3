# Composing Methods: report pipeline

Use JDK 21. The starter already runs and passes core; the task is to improve method responsibilities without changing observations. All six Java files and the package layout are required.

## Public contract

summarize(count, includeExtra, next, trace) validates count first: int 0..1000, otherwise IllegalArgumentException. Then next and trace are checked for null in that order, even when count is zero, with NullPointerException.

For each index from zero to count-1, call trace.accept("read:" + index) before exactly one next.getAsInt(). All int sample values are valid. Positive means strictly greater than zero. When includeExtra and sample>0, call trace.accept("bonus:" + index), then add sample/10 using integer truncation to the sample. Other samples remain unchanged. Sum in long, including before any addition that could overflow int. Return immutable Summary(count, positiveCount, total, mean), where mean is total/count with Java truncation toward zero, or zero when count is zero. format returns exactly count=N|positive=P|total=T|mean=M. Summary is a transparent immutable value record.

Callbacks can have state. Preserve their interleaving, call counts and exact original thrown exception object. Stop immediately on failure: a failed read trace prevents that supplier call; a failed supplier prevents its bonus trace; a failed bonus trace prevents the next sample. Prior callback effects remain observable. No callbacks occur for zero count. No computation state is retained between calls. The methods print nothing themselves and read no stdin.

## Build and checkpoints

```sh
javac --release 21 -Xlint:all -Werror -d classes @sources.txt
java -cp classes ContractTests base
java -cp classes ContractTests core
java -cp classes ContractTests object
java -cp classes Main base
java -cp classes Main
java -cp classes Main object
```

All three Main modes print:
```text
count=3|positive=2|total=30|mean=10
3
[read:0, bonus:0, read:1, read:2, bonus:2]
```

Preserve LegacyReport, Summary, Main and ContractTests. In ComposedReport, first split the sample and adjusted-value temporary. Then extract one read operation, preserving trace before the single supplier call. Extract adjustment, then a pure mean query. Record each actual green command, observation, diff and recovery point in CHECKPOINTS.md. Compare the original, helper-only design and complete MethodObjectReport example. Its private Computation fields live for one call, which increases setup and permits separate computation stages. The simpler helper design is sufficient for this fixed pipeline. A changing number of interacting stages could justify the method object; line count alone does not.

Do not replace the sampled value with repeated supplier queries. A stateful supplier can return a different value or throw. Replacing an algorithm also preserves long arithmetic, sign-sensitive division and callback timing. An IDE rename is safe only when all bindings are updated.

## Optional array adapter

Only after core: implement ComposedReport.summarizeValues(int[] values, boolean includeExtra). Reject null with NullPointerException; capture an array copy, and use its values in order through the core computation with a silent trace. Length 0..1000 follows the core range rule; longer arrays raise IllegalArgumentException. Preserve input, retain no shared state, and return the immutable Summary. All int values are valid. The complete reference supports this adapter; the starter throws a named UnsupportedOperationException for this optional operation.

```sh
java -cp classes ContractTests
```

Core is accepted independently of the optional adapter. The oracle covers exhaustive small sequences, int limits, maximum count, exact interleaved callbacks, failures and repeated calls. Green results support those behaviors; the responsibility and tradeoff explanation is separate evidence.
