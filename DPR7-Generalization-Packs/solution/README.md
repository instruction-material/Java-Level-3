# DPR7: Dealing with generalization

Three projects compare working before designs with refactored designs. The required roster project shares genuine member responsibilities. The optional report project separates format-specific responsibilities and a common algorithm. The optional event project compares composition, a redundant subclass and a genuine subtype.

The starter already produces the specified results. Behavior checks alone therefore do not complete the exercise: the candidate must also have the stated class relationships. The preserved Legacy classes supply the before behavior; the reference supplies one complete after design. Both optional projects are independent of the roster refactor.

## Run and record the baseline

Use JDK 21. From the exported project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out Main baseline
    java -ea -cp out ContractChecks baseline

The first command compiles every listed Java file. New extracted types must be added to sources.txt, one relative path per line. Keep the package directories. Recompile after every stage. If an earlier compile left removed class files, delete only the generated out folder before the next compile so a collapsed class cannot survive from an old build.

The supplied programs read no standard input. Main accepts baseline, core, transfer-baseline, transfer, extension-baseline or extension. ContractChecks accepts baseline, core, transfer, extension or full. Its explicit checks operate with either -ea or -da.

Keep the frozen Legacy classes, CompatibilityClient.java, ContractChecks.java, supplied APIs, Counter, Row and Format unchanged. Record predictions, source changes, commands and results in REFACTORING.md. Starter core and transfer checks report a missing extracted class; extension checks report the inherited List exposure. Those are the unfinished structural tasks, while the before designs remain executable.

## Required project: one shared roster model

Edit src/roster/Roster.java, Volunteer.java, Mentor.java and SponsorBanner.java. Create src/roster/Member.java and LineSource.java and list them in sources.txt. Remove RoleFormatter.java, VolunteerFormatter.java and MentorFormatter.java and their source-list entries after moving the shared line layout into Member.

A volunteer and a mentor share an exact name and session count. Their constructors currently repeat data and validation. Volunteer and Mentor also each require a matching formatter subtype. This parallel family makes adding a role require changes in two class trees. The shared Member.line layout and role hook remove that unnecessary formatter family. A sponsor banner can supply a display line but has no membership, sessions or credits.

The input and result contract is:

- Accept non-null, nonblank names and preserve accepted spaces, punctuation and case.
- Sessions are integers 0 through 40, inclusive. Each session contributes 2 credits.
- A mentor also accepts 0 through 8 mentoring slots, inclusive. Each slot contributes 3 additional credits.
- A volunteer line is name + " | volunteer | " + credits. A mentor line uses "mentor" instead.
- Validate name before sessions, then mentoringSlots for a mentor. Null name throws NullPointerException("name"); blank name throws IllegalArgumentException("name"). Bad counts throw IllegalArgumentException("sessions") or IllegalArgumentException("mentoringSlots").
- Invalid enrollment changes neither existing lines nor total. lines() returns an immutable snapshot in enrollment order. An earlier snapshot remains unchanged. totalCredits() returns a long sum.
- RosterAPI and CompatibilityClient.java keep their existing signatures and source calls.

The worked client enrolls Ada for 3 sessions and Bo for 4 sessions plus 2 mentoring slots. Both completed Main baseline and Main core print:

    Ada | volunteer | 6
    Bo | mentor | 14
    total=20
    Supported by North Lab

Ada earns 3*2. Bo earns 4*2 + 2*3. A snapshot taken after Ada retains only Ada after Bo is added. Adding a mentor with null name, sessions -1 and slots -1 reports the name error first and leaves the two successful entries intact.

Extract an abstract Member superclass from the duplicated member responsibilities. It owns name and sessions, common constructor validation, name()/sessions(), sessionCredits() and the shared line() method. Volunteer and Mentor extend it. Their public constructors retain their parameters and call super for common initialization. role() and credits() express the actual variation. Member promises a total according to the member's role; it does not promise that every member's total is only sessions*2.

Extract the LineSource interface with String line(). Member and SponsorBanner implement it. SponsorBanner stays independent of Member. A list of LineSource values can contain a Volunteer and a SponsorBanner without inventing sessions or credits for a sponsor. A complete client expression is:

    java.util.List<roster.LineSource> lines = java.util.List.of(
        new roster.Volunteer("Ada", 3),
        new roster.SponsorBanner("North Lab"));
    for (roster.LineSource line : lines) System.out.println(line.line());

This prints the volunteer line followed by the support line.

Five techniques are exercised here. Extract Superclass creates Member from the common member data and behavior. Pull Up Field moves name and sessions to that owner. Pull Up Constructor Body gives their ordered validation and initialization one home. Pull Up Method centralizes the common getters, session calculation and line layout after variation has an explicit hook. Extract Interface captures only the common display capability shared by otherwise unrelated objects.

### Substitutability, sealed types and independent variation

A Member has valid shared name/session data and supplies a role-specific nonnegative total and display line. Run the supplied contract cases for both Volunteer and Mentor. A subtype that rejects a valid shared session count, changes the first reported input error or mutates earlier observations breaks that common meaning. Member stores its fields privately and exposes a protected final sessionCredits calculation, avoiding writable protected state that a subtype could use to bypass validation.

A sealed hierarchy controls which direct subtypes exist. If the roster domain is intentionally closed to these two roles, the Member declaration can be:

    public abstract sealed class Member implements LineSource permits Volunteer, Mentor {

The supplied Volunteer and Mentor are final and in the same package, so they satisfy that declaration. A third direct subtype then fails compilation. Sealing does not prove its permitted subtypes honor input, result or state contracts; the same runtime checks still apply. The reference leaves Member open for future legitimate member types, and keeps LineSource as the separate display capability for unrelated objects.

Template Method controls a shared algorithm and its hooks. Sealing controls permitted types. Delegation controls ownership of a collaborator. These address different forces. If both membership role and output format varied independently, adding CsvVolunteer and CsvMentor would create another parallel family. Keep the formatting choice in a separate collaborator instead. The current roster promises one line format, so removing the existing duplicate formatter family is sufficient.

Run Main core and ContractChecks core after the refactor. Repeat the checks with -da. Core completion includes the identical worked output, all valid input combinations, ordered validation, immutable earlier snapshots and the actual extracted superclass/interface relationships.

## Optional transfer: format-specific reports

This is a separate saved project. Edit src/reports/ReportService.java. Create Report.java, PlainReport.java and CsvReport.java under src/reports and add those paths to sources.txt. The roster files may remain at their starter design.

The before design branches on format and repeats header/row/footer traversal. Its generic base owns delimiter and quoteLabel even for plain text. Extract PlainReport and CsvReport subclasses for the real format variations. Form Template Method by giving abstract Report a final public render(List<Row>) algorithm: validate and snapshot input, emit header, process each row in order while summing, then emit footer. Protected header(), rowText(Row) and footer(long) hooks provide the differences. Move delimiter and quoteLabel to CsvReport.

ReportService.render keeps its Format and List<Row> arguments and String result. Validate format before rows, and rows before its elements. Missing values throw NullPointerException("format"), ("rows") or ("row") respectively. Row validates label before count. Null label throws NullPointerException("label"); blank label throws IllegalArgumentException("label"). Count outside 0 through 50 throws IllegalArgumentException("count"). Preserve accepted labels exactly. Inputs and Row values remain unchanged.

Plain format has header Inventory, one label=count line per row and footer items=total. CSV has header name,count, a quoted label and integer count per row, and footer total,totalValue. Quote every label and double any embedded quote. Preserve embedded newline characters within the quoted CSV field. Every emitted line ends in a newline.

For rows ("tool, kit",2) and ("lamp",3), plain output is:

    Inventory
    tool, kit=2
    lamp=3
    items=5

CSV output is:

    name,count
    "tool, kit",2
    "lamp",3
    total,5

An empty plain report still prints Inventory and items=0; an empty CSV still prints name,count and total,0. The label say "hi" becomes the CSV field "say ""hi""". A label containing a newline remains one quoted field even though its displayed text spans lines.

Extract Subclass replaces the single class's format flag with genuine PlainReport and CsvReport roles. Form Template Method gives the common ordered algorithm one owner. Push Down Field moves the CSV delimiter out of the generic base. Push Down Method moves quoting with the format that needs it. Another format should supply its own hooks without acquiring CSV-only state.

Run Main transfer-baseline and Main transfer; both print the plain report, a separator line --- and the CSV report. Run ContractChecks transfer with -ea and -da. Complete transfer while core still has its original starter hierarchy to demonstrate that the task is independent.

## Optional extension: choose a relationship for each contract

Edit src/events/EventBuffer.java, EventFactory.java and NamedCounter.java. No new file is required. The roster and report projects may remain in their starter designs.

EventBuffer is a bounded FIFO domain model. Capacity is 1 through 8, otherwise IllegalArgumentException("capacity"). push requires non-null, nonblank event text, preserving accepted text exactly; errors are NullPointerException("event") or IllegalArgumentException("event"). A failed push leaves the buffer unchanged. Pushing to a full buffer drops the oldest event and appends the new event. next removes the oldest retained event, or throws NoSuchElementException("empty"). snapshot returns an immutable copy that remains unchanged after later operations.

Replace Inheritance with Delegation: EventBuffer owns a private collection and implements EventBufferAPI instead of extending ArrayList. The inherited indexed add/remove operations lie outside this FIFO contract and can bypass its domain rules. For example, the old public class can be assigned to a List and modified directly. The after design intentionally removes that extra list/serialization surface; migrate any such callers explicitly. Preservation here concerns the published EventBufferAPI calls, not every inherited ArrayList method or serialized old object.

Collapse Hierarchy: DefaultEventBuffer adds no domain state or behavior. Remove this class and have EventFactory.create(capacity) return EventBuffer directly through the same EventBufferAPI result. Clean out old generated classes when verifying its removal.

Replace Delegation with Inheritance: NamedCounter currently forwards every Counter operation unchanged. Counter's entire public contract is add(nonnegative long), value() and reset(). Addition uses checked long arithmetic; a negative amount throws IllegalArgumentException("amount"), overflow throws ArithmeticException, and either failure preserves the previous value. reset sets value to zero. NamedCounter adds an immutable exact nonblank label, validated with NullPointerException("label") or IllegalArgumentException("label").

Make NamedCounter extend Counter and implement NamedValue. Remove its redundant delegate and forwarding methods while retaining label(). It is a genuine Counter because all parent operations and invariants remain available unchanged. The extra label does not impose a stronger add/reset precondition. Inheritance would be a poor choice for a named counter that prohibited reset or silently narrowed allowed amounts.

With capacity2, push A, B, C. The retained snapshot is [B, C], next returns B, and the buffer becomes [C]. Add5 then2 to a counter named visits and reset it. Both extension Main modes print:

    [B, C]
    B
    [C]
    visits=7
    reset=0

Run Main extension-baseline, Main extension and ContractChecks extension with both assertion settings. Show that inherited arbitrary list mutation is absent, the empty subtype is gone, and NamedCounter can be passed to a client requiring Counter.

## Completion and interpretation

The required project is complete after core passes. The two optional projects have separate source edits and independent checks; neither is a prerequisite for core completion. The reference's ContractChecks full exercises all three completed projects.

Retain before/after outputs, validation-order cases, snapshot observations and a short explanation for each of the twelve techniques. Include the new file paths and source-list changes. Explain why SponsorBanner shares an interface without inheriting Member, why CSV details move down, why a FIFO does not expose the full List API, and why NamedCounter can inherit all Counter operations.

The before and after designs preserve the stated domain observations and the supplied recompiled source clients. Internal class topology deliberately changes. Binary linkage, reflection clients, serialization, thread safety and arbitrary callers of the old inherited collection surface are separate compatibility decisions, not promises established by these checks.

These are authored teaching cases informed by the module's refactoring category. They do not reproduce a recovered Juni application.
