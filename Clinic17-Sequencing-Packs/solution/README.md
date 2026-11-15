# Refactoring Clinic 17: Sequence several safe refactor steps

The starter's baseline and core behavior checks already pass. A step log and actual source changes establish the design work. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    tasks=3
    done=1
    remaining=15
    open=read,draw

## Plan a sequence rather than a rewrite

The working BoardReport repeats unfinished-minute calculations and mixes calculation with report formatting. These are two concrete smells with different likely change causes. Trace the public summary and remainingMinutes calls before editing and write the proposed sequence in REFACTORING.md.

Tasks are immutable. Title is validated first: null throws NullPointerException("title"); blank, longer than 16 UTF-16 char units, or containing comma, pipe, carriage return or newline throws IllegalArgumentException("title"). Minutes must be 0..120, otherwise IllegalArgumentException("minutes"). Duplicate titles and zero minutes are valid.

Both board entry points validate a non-null list with NullPointerException("tasks"), then at most eight tasks with IllegalArgumentException("tasks"), then non-null members with NullPointerException("task"). Preserve caller data and encounter order. remainingMinutes sums only tasks whose done flag is false.

summary returns exactly four lines: "tasks=N\n", "done=D\n", "remaining=M\n" and "open=TITLES\n". TITLES are unfinished titles in encounter order, comma-separated without added spaces; "-" represents none. Retain duplicate titles. The sample [read(10,false),code(20,true),draw(5,false)] produces tasks=3, done=1, remaining=15 and open=read,draw. Empty input gives zero counts and open=-.

Use these checkpoints:

1. Extract the duplicated unfinished-minute loop into one private method. Run baseline and keep this small passing state.
2. Move analysis into an owned BoardAnalysis while formatting stays in BoardReport. Validate the input once at that ownership boundary and produce a BoardStats value. Run baseline again and decide whether the planned next step is still safe.
3. Move only formatting to BoardText, retaining the public facade and exact output. Run core and the unchanged external client.

BoardStats is a frozen snapshot: tasks 0..8, done 0..tasks, remaining 0..960, and an unmodifiable copied openTitles list with size tasks-done. Its constructor validates those fields in that order and uses their field names in errors. Null members are rejected by List.copyOf. Derived stats retain unfinished titles in original order.

The reference performs one analysis pass, stores immutable state and lets a renderer consume the resulting value. Class counts alone do not show improvement: identify the responsibility moved and a change now confined behind that boundary.

Retain the actual source/results for both intermediate checkpoints and the final design. Replay a retained earlier source in an isolated working copy to demonstrate a recoverable checkpoint. Keep personal work intact. Name a first proposed step that would be too broad and split it. Sorting titles changes observable output, so postpone it from the behavior-preserving core and evaluate it separately.

## Independent transfer: locate the first bad checkpoint

Complete SnapshotDiff.firstDifference(before,after). Validate and copy before first, then after, with NullPointerException("before") or ("after") for null lists. Null captures are rejected. Compare exact strings, including whitespace and newlines, without modifying the lists.

Return the first unequal index. If all shared positions match but lengths differ, return the shared length. Return -1 only when both sequences are identical. ["before","step1","final"] versus ["before","bad","final"] gives 1, even though the final captures match. Empty versus one entry gives 0; two empty sequences give -1.

Retain an intermediate mismatch, a length mismatch, exact newline difference and unchanged inputs. This protects a recorded checkpoint sequence, not the unrecorded behavior between snapshots.

## Independent extension: a separate sorted preview

Complete BoardPreview.sorted(tasks). Apply the same complete task-list validation, retain the original list and return a new unmodifiable list sorted by title using String's natural order. Equal titles retain their original relative order; duplicate tasks remain. Empty input returns an empty list.

The sample preview titles are [code,draw,read], while the core report still says open=read,draw. This new presentation is separate from changing the established core order. Retain first/last titles, stable duplicates with different minutes/done flags, empty input and input purity. Explain why a preview was safe to add without changing the characterized report.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import api.*;
    import app.BoardReport;
    import java.util.List;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            BoardAPI report=new BoardReport();
            List<Task> tasks=List.of(new Task("z",5,false),new Task("a",2,true));
            if(!report.summary(tasks).equals("tasks=2\ndone=1\nremaining=5\nopen=z\n"))
                throw new AssertionError("unchanged board");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
