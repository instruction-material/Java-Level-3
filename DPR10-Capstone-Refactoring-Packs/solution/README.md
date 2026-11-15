# DPR10: Capstone Refactoring Studio

Refactor a working workshop-registration application in small, behavior-preserving steps. The supplied starter manages several sessions, seat limits, an ordered waiting list, immutable snapshots, an event history, two reports and a command interface. Its parallel maps and mixed responsibilities create a useful diagnosis and extraction problem. The reference is one defensible design; the capstone is evaluated through working behavior, reviewable changes and explained decisions.

A different medium Java application is suitable when it runs reproducibly, contains several interacting responsibilities and has observable behavior worth preserving. First record its entry points, sample inputs, outputs, errors and dependencies. Establish characterization checks before changing its structure. Use its own tests and commands in place of this pack's workshop-specific oracle. Keep the same diagnosis, safe-step, architecture and reflection evidence described below.

## Run before changing code

Use JDK 21 in the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -ea -cp out ContractChecks core
    java -cp out Main baseline
    java -cp out Main core

Both baseline and core behavior checks pass in the starter. Passing them establishes the starting behavior; it does not establish that a refactor has happened. Main baseline uses the frozen LegacyWorkshopDesk; Main core uses the candidate WorkshopDesk. They print the same two reports and history:

    Workshops 2
    room capacity=2 open=true
    enrolled=bo,cy
    waiting=-
    quiet capacity=1 open=false
    enrolled=-
    waiting=-
    2
    room|2|open|bo,cy|-
    quiet|1|closed|-|-
    [open, join:ana:enrolled, join:bo:enrolled, join:cy:waiting, leave:ana:enrolled, promote:cy]

Every displayed line ends with a newline. Demo and check modes need no standard input. To exercise the command interface, run:

    java -cp out Main script-baseline < sample-input.txt
    java -cp out Main script-core < sample-input.txt

Input redirection is a terminal command. Without redirection, those two modes read lines from standard input until end of input. They write console output and keep state only in memory. No mode contacts a network, database or external service. The sample produces:

    opened
    ENROLLED
    WAITLISTED
    true
    1
    room|1|open|bo|-
    true
    error:IllegalStateException:closed

Keep the API package, LegacyWorkshopDesk, AppScript, Main, CompatibilityClient, Step, Observation and ContractChecks unchanged. Modify WorkshopDesk and add cohesive implementation classes as needed. Include new Java files in sources.txt. Preserve public construction as new WorkshopDesk() and the DeskAPI methods.

## Required project: diagnosis, sequence and implementation

Read WorkshopDesk before opening the reference. Identify at least three concrete issues with source locations and consequences. For example, capacities, open flags, enrolled people, waiting people and history are kept in five maps keyed by the same session ID. Creating a session must keep all five maps consistent. The same class changes both when queue policy changes and when report formatting changes.

Write behavior promises in REFACTORING.md before selecting techniques. Prioritize one small extraction, then move a responsibility with its data, then isolate reporting. Predict the risk and protective check for each step. A possible sequence is:

1. Extract a private membership predicate without changing validation or mutation order. Compile and run baseline/core.
2. Group one session's state into a session object. Move the corresponding join/leave/close behavior with that state. Keep WorkshopDesk's public API and session insertion order.
3. Move report rendering to a collaborator that consumes immutable snapshots. Keep exact output and query purity.
4. Review names, visibility and package dependencies. Remove temporary duplication after the migrated path is protected.

Record actual source changes and results after each step. Use separate small commits when working in Git, or retain dated before/after files and results. A single passing final run cannot demonstrate safe intermediate transformations. The frozen legacy copy remains a behavior reference, not a base class for the candidate.

### Observable behavior to preserve

Session and participant IDs match [a-z][a-z0-9-]{0,15}; accepted input is exact, with no trimming or case conversion. Null IDs throw NullPointerException and other invalid IDs throw IllegalArgumentException, with message "session" or "participant". Capacity is an integer 1 through 4; other values throw IllegalArgumentException("capacity"). open validates session, then capacity, then rejects an existing ID with IllegalStateException("session"). No existing session can be reopened.

join and leave validate session then participant before looking up the session. Missing sessions throw NoSuchElementException("session"); a closed session throws IllegalStateException("closed") before duplicate/no-op decisions. join returns ENROLLED when there is a seat, WAITLISTED when added to the queue and DUPLICATE when already enrolled or waiting. Each session has its own roster; the same participant can join another session.

leave returns false for an absent participant and true for a removal. Removing a waiting participant changes only that queue. Removing an enrolled participant promotes the earliest waiting participant, appending them to enrolled order. Arrival order matters: cy waiting before bo is promoted first even though bo sorts earlier. close returns true once, appends one event, retains the roster and queue, and returns false on repetition.

SessionView records id, capacity, open, enrolled, waiting and history. All returned lists, including all(), are unmodifiable snapshots. Old snapshots remain unchanged after later operations. view, all and report never mutate state or append history. Session order is opening order; enrolled and waiting order is arrival order after removals/promotions. Different desk instances own independent state.

History begins with "open". Successful joins append "join:ID:enrolled" or "join:ID:waiting". Removal appends "leave:ID:enrolled" or "leave:ID:waiting"; promotion then appends "promote:ID". First close appends "close". Duplicates, absent removals, repeated close and failures add no events.

report validates style first: null throws NullPointerException("style"), and anything except exact "plain" or "compact" throws IllegalArgumentException("style"). Plain output begins "Workshops N", then three lines per session: "ID capacity=C open=BOOLEAN", "enrolled=IDS" and "waiting=IDS". Compact begins "N", then "ID|C|open-or-closed|ENROLLED|WAITING". Lists use comma-separated IDs without spaces and "-" for empty. Every line, including the last, ends with newline.

AppScript.run takes a non-null desk and a non-null list of non-null lines, validating the whole list before operations. Each trimmed nonblank line has whitespace-separated words. Commands are open ID CAPACITY, join ID PERSON, leave ID PERSON, close ID, view ID and report STYLE. Wrong arity or unknown commands throw IllegalArgumentException("command"). Capacity text uses Integer.parseInt; parse failures become IllegalArgumentException("capacity"). Parsing completes before domain validation. Each RuntimeException becomes "error:SimpleClass:message" followed by newline; later lines still run. Successful open prints "opened"; join prints its enum name; leave/close print true or false. view prints "ID|CAPACITY|BOOLEAN|[enrolled]|[waiting]|[history]". report prints its full report without an extra blank line.

### Final review and reflection

Explain which responsibilities moved, which state now belongs together, which collaborations are visible and which imports point across packages. Show one change that would now affect fewer files and one remaining coupling. Use before/after source evidence rather than class counts alone.

The reference keeps WorkshopDesk as a public facade, uses a package-private SessionBook per session and a package-private DeskReports renderer. The two fixed report formats need only a helper here. A Strategy becomes useful if callers need independently supplied report policies; that is a possible future requirement, not a reason to add an interface now. Similarly, open/closed alone does not require a State hierarchy, and ordinary construction does not require a Factory.

Discuss one technique that paid off, one pattern that emerged or remained unnecessary, and one postponed behavior change. Sorting the waiting list is an observable behavior change: demonstrate the cy-before-bo counterexample. A rename operation, persistence or networking would also introduce new behavior. Evaluate them separately from this refactor.

Submit a smell/risk map, ordered step log with passing evidence, final code and commands, before/after responsibility sketch, exact behavior example, pattern decision and reflection. These artifacts support individual reading or a walkthrough with an instructor.

## Optional transfer: compare complete traces

Complete trace.Equivalence.same(before, after, steps) while leaving the core and rename exercise at starter if desired. Supply two independently constructed DeskAPI objects. Validate before, after and the complete steps list in that order before any operation. Null arguments report NullPointerException("before"), ("after") or ("steps"); a null step is rejected before calls. Using the same desk object twice throws IllegalArgumentException("independent desks").

Apply every Step to each desk in order. Observation.take captures each step's returned text or RuntimeException's fully qualified class and message, followed by all session snapshots. Return false at the first different observation; return true only after all match. An empty plan returns true without calls. Preserve the supplied list. RuntimeException from a step is part of the observation; failure while obtaining a snapshot propagates. Existing state is allowed, so construct fresh desks when comparing identical initial conditions.

    List<Step> plan = List.of(
        new Step.Open("room", 1), new Step.Join("room", "ana"),
        new Step.Join("room", "cy"), new Step.Join("room", "bo"),
        new Step.Leave("room", "ana"), new Step.Report("compact"));
    boolean same = Equivalence.same(
        new LegacyWorkshopDesk(), new WorkshopDesk(), plan);

Run ContractChecks transfer with -ea and -da. Explain why comparing only final rosters misses wrong return values, wrong error messages and a temporary snapshot defect that later disappears. Add a separate probe with one matching trace and one deliberate mismatch. Keep the supplied oracle intact.

## Optional extension: preview a participant rename

Complete preview.RenamePreview.rename(snapshot, oldID, newID). This deliberately adds behavior after the refactor and remains independent of it. Accept a valid application-produced SessionView and return a preview; the live desk stays unchanged.

Validate snapshot, oldID and newID in that order. Null snapshot throws NullPointerException("snapshot"); ID rules and messages use "oldID" and "newID". A closed snapshot throws IllegalStateException("closed") before no-op handling. If oldID is absent or equals newID, return the exact input snapshot. Otherwise reject a target ID already enrolled or waiting with IllegalArgumentException("newID"). Replace oldID in its current list position, retain every prior history entry verbatim and append exactly "rename:OLD:NEW". Preserve session ID, capacity and open flag. Return new immutable lists and retain the input's lists unchanged.

For an open snapshot with enrolled=[ana], waiting=[cy, bo], renaming cy to dee yields enrolled=[ana], waiting=[dee, bo]. Its history keeps "join:cy:waiting" and appends "rename:cy:dee". Moving dee to the end or rewriting old history would change the contract.

Run ContractChecks extension under both assertion settings. Write PreviewProbe.java:

    import api.*;
    import legacy.LegacyWorkshopDesk;
    import preview.RenamePreview;
    import java.util.List;
    public final class PreviewProbe {
        private PreviewProbe() {}
        public static void main(String[] args) {
            DeskAPI desk = new LegacyWorkshopDesk();
            desk.open("room", 1);
            desk.join("room", "ana");
            desk.join("room", "cy");
            desk.join("room", "bo");
            SessionView before = desk.view("room");
            SessionView after = RenamePreview.rename(before, "cy", "dee");
            if (!after.waiting().equals(List.of("dee", "bo")))
                throw new AssertionError("queue position");
            if (!before.equals(desk.view("room")))
                throw new AssertionError("live state changed");
            System.out.println("preview passed");
        }
    }

Compile with javac --release 21 -cp out -d out PreviewProbe.java and run java -cp out PreviewProbe. Retain a collision failure and a closed-session failure as additional evidence. Explain why this is a separate feature with a new contract.

After completing all three projects, run ContractChecks all. Explicit failures remain enabled with either -ea or -da. The supplied checks cover specific observations and bounded model-based sequences; the capstone portfolio establishes the refactoring argument and remaining risks.
