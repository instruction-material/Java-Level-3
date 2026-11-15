# DPR9: Testability, DI and refactoring with confidence

The required project refactors a working journal that obtains real time, random identifiers and global console output directly. Preserve its public construction, record call, output shape and validation. Constructor seams then allow exact test inputs and recording fakes. The optional capture-normalization and sink-failure exercises are separate attempts and can be completed while the journal stays at starter.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core

Both Main modes print one journal line for lamp(2). Its format is ISO-instant|id|lamp|2 followed by a newline. Time is the current UTC instant and ID is a random integer from 0 through 999999. Two executions need not print identical time or ID, and a collision is possible. These programs read no standard input and write only console output. They perform no file, database or network operations.

ContractChecks baseline characterizes both the frozen LegacyJournal and the candidate's default behavior. It captures global console output temporarily, checks the returned values against that output, validates the time interval and ID bounds, and restores the original output stream. A single-process console-capture test should run in isolation from concurrent output tests. The injected checks avoid changing global output.

Keep Entry, JournalAPI, EntrySink, LegacyJournal, CompatibilityClient and ContractChecks unchanged. Record predictions, small changes and results in REFACTORING.md. The starter's baseline works; completed core checks report the missing injection constructor, normalization reports an unmatched capture, and retry reports its first IOException until those tasks are finished. Explicit checks operate with -ea or -da.

## Required project: isolate the journal's dependencies

record(label,count) accepts a non-null, nonblank label without pipe, carriage return or newline and preserves accepted spaces, punctuation and Unicode. Count is an integer 0 through 99. Validate label before count and before consulting dependencies. Null label throws NullPointerException("label"), other invalid labels throw IllegalArgumentException("label"), and invalid count throws IllegalArgumentException("count"). Invalid input produces no output and makes no dependency calls.

The default new DispatchJournal() construction remains available. Its record method obtains time, obtains an ID, writes exactly one immutable Entry to its output sink and returns that same value. The output line has exactly four pipe-delimited fields and a final newline. The core performs one output attempt. A sink IOException becomes UncheckedIOException with detail message "journal" and the original exception as cause. Runtime failures propagate. A collaborator may already have performed work before failing; this boundary does not promise rollback of outside effects.

Add DispatchJournal(Clock clock, IntSupplier ids, EntrySink sink). Validate these constructor arguments in that order with NullPointerException messages "clock", "ids" and "sink". Preserve the default constructor by wiring a UTC system clock, the original random-ID policy and a console sink. The supplied EntrySink contract is a one-method interface whose write(Entry) may throw IOException. No container or dependency-injection framework is needed.

Refactor in small steps. First isolate time acquisition, then ID acquisition, then output. Run baseline after each move. Introduce the alternate constructor and store its collaborators privately; record uses those supplied collaborators. The old default entry point becomes the assembly location and the method contains the business sequence. A private helper alone improves readability, while a caller-supplied seam makes a dependency controllable in a test.

For a clock fixed at 2026-11-15T12:00:00Z, an ID supplier returning 17, label lamp and count 2, the returned entry and captured sink line are exactly:

    2026-11-15T12:00:00Z|17|lamp|2

The line ends in a newline. A recording fake observes clock, id, sink in that order, once each. The same collaborators with a null label and count -1 observe no calls and report the label error. With valid input and an ID supplier returning -1 or 1000000, Entry rejects the ID before the sink is consulted. A fixed valid time or repeated ID is intentional test input; the supplier contract does not require a distinct result every time.

A deterministic standalone check after the refactor can be written as Probe.java:

    import journal.*;
    import java.time.*;
    import java.util.*;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            List<Entry> written = new ArrayList<>();
            JournalAPI journal = new DispatchJournal(
                Clock.fixed(Instant.parse("2026-11-15T12:00:00Z"), ZoneOffset.UTC),
                () -> 42, written::add);
            Entry value = journal.record("kit", 0);
            if (!value.line().equals("2026-11-15T12:00:00Z|42|kit|0\n"))
                throw new AssertionError("line");
            if (!written.equals(List.of(value))) throw new AssertionError("sink");
            System.out.println("probe passed");
        }
    }

Compile it with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Add one further valid case and one invalid-input/no-sink-call case to this separate probe. Explicit failures still execute when Java's assert keyword is disabled. Preserve the supplied ContractChecks oracle; learner-written checks belong in their own file.

Run ContractChecks core under both assertion settings. Its recording collaborators test every valid count for several exact labels, dependency order, constructor errors, ID boundaries and a failing sink. Baseline characterization preserves observable default promises; injected checks establish exact controlled results. A failure in an assertion identifies an observed mismatch, while the changed responsibility or seam explains why the design is easier to test.

## Optional transfer: characterize volatile output carefully

Complete CaptureNormalizer.normalize(String line) independently of the journal refactor. It accepts one complete canonical journal line, validates all four fields and masks only the time and ID. The label and count stay exact. For example:

    2026-11-15T12:00:00Z|17|lamp|2

normalizes to:

    <time>|<id>|lamp|2

Both strings include their final newline. Different valid instants and IDs can share this normalized result, while changing count from 1 to 2 or changing the label must change the result. A rule that removes all numbers would erase a count defect. A rule that approves an empty capture would conceal missing output.

Null line throws NullPointerException("line"). Other malformed input throws IllegalArgumentException("line"): missing or extra newline, carriage return, wrong number of fields, invalid Instant, out-of-range or noncanonical ID/count, or an invalid label. Canonical integers have no plus sign, surrounding whitespace or leading zero except zero itself. Count is 0 through 99 and ID is 0 through 999999. Preserve spaces inside an accepted label.

Run ContractChecks transfer with -ea and -da. Retain one actual default capture, its normalized approval, a changed-count counterexample and a malformed capture that the checker rejects. Compare a characterization approval with an exact deterministic fake-based check: each protects the fields and environment it actually observes. The normalization task can pass with DispatchJournal still at starter.

## Optional extension: deliberate failure behavior

Complete RetryingSink as an explicit new feature, separate from the behavior-preserving core. Its delegate promises that an IOException means the entry was not committed. Under that contract, retry that same immutable Entry once after an IOException. First-attempt success makes one call; a transient failure followed by success makes two. Two IOExceptions produce exactly two calls and propagate the second exception, retaining the first as suppressed when the exception objects differ. A runtime failure is propagated after one call. Null delegate and entry report NullPointerException("delegate") and NullPointerException("entry") before a delegate call.

Use recording fakes instead of a real unreliable service. For entry (1970-01-01T00:00:00Z,17,"lamp",2), predict these traces:

- Success: one attempt with that exact entry.
- IOException("first"), then success: two attempts with the same object.
- IOException("first"), then IOException("second"): two attempts; second is thrown and first is retained.
- IllegalStateException("bad"): one attempt and the same runtime failure.

Run ContractChecks extension with -ea and -da. This exercise can pass while the journal and normalization remain at starter. Adding retries changes failure behavior; its new checks supplement rather than redefine the core's one-attempt promise. A real sink that can write partly, commit before losing an acknowledgment or expose duplicate effects needs idempotence or reconciliation before this retry policy is suitable.

## Reviewable steps and confidence

For each meaningful step, retain the last passing result and a small source change. Identify the test that protects each behavior promise, the seam that removes an environmental dependency, and a change postponed because it alters behavior. Compare a fixed-time or failed-output case that previously required controlling a process-wide dependency with the corresponding small injected check. Constructor injection separates assembly from use; a fake records only its declared calls and cannot establish the behavior of a real external service.
