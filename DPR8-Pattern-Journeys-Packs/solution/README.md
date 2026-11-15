# DPR8: Refactoring toward patterns

Refactor three working designs in small steps. The required notice project separates format creation, a common rendering algorithm and ordering. The optional ticket project compares three destinations for one state-code smell. The optional editor project separates an external date boundary, requests, history and opaque saved state. Complete each optional project in its own attempt; neither depends on completing the other projects.

The starter already preserves the specified behavior. A completed refactor must preserve that behavior and implement the stated responsibility changes. Frozen Legacy implementations provide before behavior, and the reference supplies one complete after design. Keep the supplied APIs, Notice, AncientDay, CompatibilityClient and ContractChecks unchanged.

## Compile and establish the baseline

Use JDK 21. From the exported project directory:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -ea -cp out Main baseline

List every extracted Java file in sources.txt, one relative path per line, and preserve package folders. Recompile after each step. Only the generated out directory may be removed when stale compiled types need clearing. The programs read no standard input. Main accepts baseline, core, transfer-baseline, transfer, extension-baseline or extension. ContractChecks accepts baseline, core, transfer, extension or full. Explicit checks run with assertions enabled or disabled.

Baseline checks pass for both roles. The untouched starter intentionally fails the three completed-mode checks because the extracted creator, state and snapshot types do not yet exist. Record each step, commands, results and stopping decision in REFACTORING.md.

## Required project: notice creation and ordering

NoticeService.render(style, notices, reverse) retains its public signature. Styles are exactly text and bracket. A Notice contains a non-null, nonblank single-line label, preserved exactly, and an integer count from 0 through 9. A label containing carriage return or newline is invalid. Validate label before count: null label throws NullPointerException("label"), other invalid labels throw IllegalArgumentException("label"), and invalid count throws IllegalArgumentException("count").

Validate style before the list, then elements from first to last. Null style throws NullPointerException("style"); unknown style throws IllegalArgumentException("style"). Null list and element throw NullPointerException("notices") and NullPointerException("notice"). Neither rendering nor reversal mutates the caller's list or values.

The output begins with Notes and ends with end=N, where N is the number of rows. Every line, including the footer, ends in a newline. Text rows are label=count. Bracket rows are [label:count]. Brackets and punctuation inside labels are literal; these display formats are not CSV or a round-trip data encoding.

For lamp(2), kit(3), style bracket and reverse=true, both Main baseline and completed Main core print:

    Notes
    [kit:3]
    [lamp:2]
    end=2

The same values in text/original order print Notes, lamp=2, kit=3 and end=2 on four newline-terminated lines. An empty input prints only Notes and end=0. The caller's lamp, kit list remains in that order after the reversed rendering.

Start with Extract Method: isolate the row conversion while leaving validation, traversal and output unchanged. This first step can be expressed inside NoticeService as:

    private static String formatRow(String style, Notice row) {
        if (style.equals("text")) return row.label() + "=" + row.count() + "\n";
        return "[" + row.label() + ":" + row.count() + "]\n";
    }

Replace only the existing per-row conditional with result.append(formatRow(style, row)); run baseline checks. Move that operation into a NoticeLine collaborator and extract creator classes when adding a format requires editing the same traversal. Keep input validation before any formatting work. A static NoticeCreator.forStyle moves construction behind a named function. This is the Replace Constructor with Factory Method refactoring seam; the subclass-based Factory Method pattern requires the next step too.

Create abstract NoticeCreator with protected abstract NoticeLine createLine(). TextCreator and BracketCreator override this method to supply the product. Its final public render(List<Notice>, Ordering) validates and snapshots rows, asks the ordering strategy for the sequence, emits the header, creates a product, renders each row and emits the footer. This is a Template Method around an actual overridable Factory Method. The small forStyle switch selects a creator at the boundary and remains a reasonable closed-set decision.

Extract Ordering.arrange(List<Notice>) as a Strategy. OriginalOrder returns an unchanged snapshot and ReversedOrder returns a reversed snapshot. NoticeService selects the strategy from its existing reverse argument. The strategies preserve values and input ownership. A new caller-supplied ordering can now be passed to a creator without rewriting the shared rendering algorithm.

Create NoticeCreator.java, NoticeLine.java, TextCreator.java, BracketCreator.java, Ordering.java, OriginalOrder.java and ReversedOrder.java under src/notices. Refactor NoticeService.java and update sources.txt. Run ContractChecks core with -ea and -da, then Main core. Keep ticket/editor code at starter if desired.

The reference is useful when formats are added independently and creators own genuinely different product construction. With exactly two stable one-line formats, a private extracted method or a map of formatting functions may be the better stopping point. An ordering flag alone also does not demand a class hierarchy. Record the change pressure before choosing the full exercise design; the structure check verifies the requested practice design, not a universal best architecture.

## Optional transfer: one smell, three destinations

TicketAPI.apply(action) accepts exactly approve, finish and reset. The initial status is DRAFT. Only approve changes DRAFT to READY, finish changes READY to DONE, and reset changes DONE to DRAFT. Each successful transition returns true and appends OLD->NEW to history. Other recognized actions return false and change nothing. Null action throws NullPointerException("action"); an unrecognized action throws IllegalArgumentException("action") without changing status or history. history() returns an immutable snapshot that remains unchanged after later transitions.

Both ticket demo modes print:

    finish=false:DRAFT
    approve=true:READY
    approve=false:READY
    finish=true:DONE
    reset=true:DRAFT
    [DRAFT->READY, READY->DONE, DONE->DRAFT]

Compare three complete implementations of this same contract. GuardTicket uses guards and a small shared move helper. StrategyTicket.standard supplies a selectable TransitionPolicy; the engine owns the current status and the policy supplies the next status. Ticket is the candidate to refactor into State: a private TicketState field identifies its current state, and each enum constant implements next(action). The context delegates to its state, records successful movement and retains the public String status API.

Create src/tickets/TicketState.java, refactor Ticket.java and update sources.txt. Run ContractChecks transfer with both assertion settings. The supplied tests compare every recognized-action sequence up to length five against the frozen baseline and both smaller alternatives, including rejected actions, full cycles, immutable earlier histories and invalid inputs.

Choose a destination using a concrete future request. A stable three-transition lifecycle favors guards. Different customer-selectable approval policies favor Strategy. States that each acquire substantial entry, exit and action behavior favor State. A new required status or changed approval rule is a feature change; separate its new expectations from the baseline refactoring checks. State objects may still use simple action conditions, and a single switch is not evidence that polymorphism is automatically needed.

## Optional extension: editor boundaries and undo

EditorAPI keeps append(year, month, day, text), undo(), redo() and text(). Dates are Gregorian with year 1000 through 9999. AncientDay is the supplied external type: y(), m() and d() expose its date. Out-of-range year throws IllegalArgumentException("year"); other invalid dates throw java.time.DateTimeException. Date validation precedes text validation. Null text throws NullPointerException("text"); empty text throws IllegalArgumentException("text"). Accepted text, including spaces, Unicode and embedded newlines, is preserved exactly.

A successful append adds ISO-date + " | " + text + newline. Undo restores the entire previous text, redo restores the complete undone result, and each returns false at an empty stack. A new successful append after undo clears redo; an invalid append preserves text and both stacks. No operation reads a clock or accesses files or the network.

Append (2026,11,15,"lamp"), then (2026,11,16,"kit"). The text is:

    2026-11-15 | lamp
    2026-11-16 | kit

Undo returns true and leaves only the lamp line. Redo returns true and restores both lines. Undo again and append (2026,11,17,"c"): redo then returns false, and the two lines are lamp followed by c on November 17. An invalid February 29 in 2025 after undo must leave the valid redo available.

First extract date conversion from the editor. DayExtension wraps AncientDay and adds iso() and weekend(); November 14 and 15, 2026 are weekend dates, while November 16 is not. This is a Local Extension around an unmodifiable API. DayAdapter exposes the client-required NoteDate.label() contract and delegates to the extension. The adapter addresses interface mismatch; the extension adds domain operations. When only one formatting operation is required, a direct adapter can combine these two wrappers. The separate exercise types make their responsibilities inspectable.

Extract TextBuffer, which owns the text and creates/restores an opaque nested Memento. Memento construction and fields stay private and final; the buffer rejects another buffer's snapshot with IllegalArgumentException("owner") before changing text. The caretaker can retain a snapshot without inspecting or rewriting its contents.

Introduce EditCommand with execute/undo/redo. AppendCommand owns its receiver and validated date/text, captures before and after snapshots on its first execution, and restores those exact snapshots for undo/redo. CommandHistory owns executed and undone stacks. Move a command between stacks only after its operation succeeds; executing a successful new command clears the redo stack. Editor becomes the Facade that hides date conversion, buffer, command creation and history from callers. This also applies Hide Delegate: clients no longer traverse those collaborators to perform an editor operation.

Create DayExtension, NoteDate, DayAdapter, TextBuffer, EditCommand, AppendCommand and CommandHistory under src/editor. Refactor Editor.java and update sources.txt. Run ContractChecks extension with both assertion settings and compare both editor demo modes. The tests cover every four-operation sequence up to length five, Unicode/newline preservation, calendar boundaries, invalid input and branch replacement. Keep the notice and ticket code at starter if desired.

Command represents a request and its receiver/arguments. Memento represents saved receiver state. History orders completed requests. A simple string-only editor with one mutation could reasonably keep its two snapshot stacks directly, as the working baseline does. Independent toolbar, shortcut and queued invocations make Command more useful; richer private receiver state makes opaque Memento more useful. When those pressures disappear, merge collaborators instead of retaining layers solely for their pattern names.

## Review the arrival and stopping point

For each project, retain matching baseline and candidate results, the last passing intermediate step and one change scenario. Identify the smallest step that supplied the main benefit, the extra seam the named pattern supplied, the cost in types and indirection, and a concrete exit trigger for simplifying it. A complete reference satisfies the full checks; each optional project also passes independently while the other two remain at starter.

The pattern terminology follows the linked sources in SOURCES.md. Compare the static construction refactoring with the subclass-based Factory Method description, and compare externally selected Strategy with behavior selected by current State. Those distinctions determine what the code actually demonstrates.
