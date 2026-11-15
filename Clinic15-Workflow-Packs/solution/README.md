# Refactoring Clinic 15: Extract a shared workflow and deliberate hooks

The starter's original public behavior and Main core work. The post-refactor core check reports 'Complete template skeleton' until the new required design entry points are completed. Transfer and extension each report their named unfinished operation until completed. Their source files can be finished independently.

## Run the working before design

Use JDK 21 from the extracted project folder:

    javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d out @sources.txt
    java -ea -cp out ContractChecks baseline
    java -da -cp out ContractChecks baseline
    java -cp out Main baseline
    java -cp out Main core
    java -cp out CompatibilityClient

Baseline compares the frozen legacy implementation and the candidate's existing public behavior. Both Main modes print the same result:

    begin
    a
    b
    end

## Identify the common sequence

The working DocumentService duplicates a document workflow: validate rows, create a header, append each row in order and finish with a footer. Plain output for ["a","b"] is "begin\na\nb\nend\n"; marked output is "BEGIN\n* a\n* b\nEND\n". Every shown line ends with newline. Empty input still has its header and footer. An empty row remains an actual blank body row.

The facade validates kind non-null with NullPointerException("kind"), then the entire row list, then checks whether kind is exactly plain or marked. Unknown kinds throw IllegalArgumentException("kind"). Row validation rejects a null list with NullPointerException("rows") and more than six rows with IllegalArgumentException("rows"). Each row must be non-null (NullPointerException("row")), at most 16 UTF-16 char units and contain no pipe, carriage return or newline (IllegalArgumentException("row")). Empty rows and accepted spaces are preserved. Input order and caller lists remain unchanged.

Extract the repeated row loop into a helper first. Run baseline with both formats before adding variation. Then complete DocumentTemplate's final build method: validate all rows, call header once, line once per row in order, and footer once. Concatenate exactly those strings. Reject null hook results with NullPointerException("header"), ("line") or ("footer"). A hook runtime failure propagates and stops later hooks. Earlier callback effects have no rollback guarantee.

header and line are protected abstract hooks. The default footer is an empty string, permitting a deliberate omitted footer. PlainDocument and MarkedDocument implement their existing formats. Keep DocumentService.build(kind,rows) and new DocumentService() intact. The reference facade selects a format and delegates to the shared final skeleton; individual variants own their formatting.

Compile a custom template with no footer and check its output. Record a trace such as header, line:a, line:, footer and verify that invalid later rows cause no hooks. A final template method keeps order fixed while subclasses supply steps. A Strategy-based design can instead receive independent step collaborators when runtime assembly or multiple unrelated behaviors warrant it; compare that tradeoff rather than introducing both forms by default.

Retain both original outputs, the first shared-loop extraction, the final skeleton, an omitted-footer case, a failing body hook and an explanation of which duplication disappeared.

## Independent transfer: observe workflow stages

Complete WorkflowTrace.stages(List<String> rows,StepHook hook). Validate and snapshot all rows with the same policy, then validate hook with NullPointerException("hook"). For valid input invoke hook.accept("start",""), then ("row",row) for each row, then ("end",""). Return an unmodifiable list containing "start", each "row:"+row, and "end". Input remains unchanged.

For ["a",""], the returned list is ["start","row:a","row:","end"], while hook arguments distinguish the stage name from its value. An empty input still calls start and end. A hook failure propagates and stops further calls. Record the actual call trace; a list constructed without invoking the hooks does not demonstrate collaborator order.

This exercise uses its own StepHook boundary and can finish with DocumentTemplate still unfinished. Retain a normal trace, empty trace, later-invalid input with no calls and one stopped trace.

## Independent extension: numbered output and optional footer

Complete NumberedRows.render(rows,footer). Apply the same whole-list validation and preserve input. Return "ROWS\n", then one-based "NUMBER:ROW\n" lines in original order. If footer is true, append "total=N\n"; otherwise append nothing. Empty input produces only the header, plus "total=0\n" when requested.

For ["a",""] with footer=true, output is "ROWS\n1:a\n2:\ntotal=2\n". Retain both footer choices, an empty input and a validation failure. This is a new format rather than a change to either original format.

## Finish and review

After the required refactor, run:

    java -ea -cp out ContractChecks core
    java -da -cp out ContractChecks core

For each chosen independent exercise, run its ContractChecks transfer or extension mode under both assertion settings and Main with that mode. After all three projects are complete, run ContractChecks all. Every demo/check mode reads no standard input. The supplied application uses in-memory values and console output; its fixtures model callbacks without contacting external services.

Preserve the api package, legacy implementation, check helper, ContractChecks, Main and CompatibilityClient. Keep learner-written checks in a separate file and add new Java source paths to sources.txt. The reference is one design, and the frozen checks cover stated observations; the review record supplies the ownership, risk and design argument.

Write this separately compiled Probe.java after completing core:

    import app.DocumentTemplate;
    import java.util.List;
    public final class Probe {
        private Probe() {}
        public static void main(String[] args) {
            DocumentTemplate custom=new DocumentTemplate() {
                protected String header(){return "H\n";}
                protected String line(String row){return row+"\n";}
            };
            if(!custom.build(List.of("a")).equals("H\na\n"))throw new AssertionError("optional footer");
            System.out.println("probe passed");
        }
    }

Compile with javac --release 21 -cp out -d out Probe.java and run java -cp out Probe. Expected output is "probe passed" followed by newline. The public client demonstrates the required boundary independently of the supplied checker.
