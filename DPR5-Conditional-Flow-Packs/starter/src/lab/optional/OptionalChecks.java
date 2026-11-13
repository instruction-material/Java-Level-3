package lab.optional;
import java.util.ArrayList;
import java.util.List;
import lab.*;
import lab.BookingData.*;

public final class OptionalChecks {
    private static int checks;
    private OptionalChecks() {}
    private static void equal(Object expected, Object actual, String label) {
        checks++;
        if (!java.util.Objects.equals(expected, actual))
            throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
    }
    private static final class RecordingReporter implements StatusReporter {
        private final List<String> events = new ArrayList<>();
        @Override public void started() { events.add("started"); }
        @Override public void completed(Decision decision, int credits) {
            events.add("completed:" + decision.reason() + ":" + credits);
        }
        @Override public void failed(RuntimeException error) { events.add("failed:" + CoreChecks.errorKey(error)); }
    }
    private static int price(Machine machine, int minutes) {
        return switch (machine) {
            case STANDARD -> Math.floorDiv(minutes - 1, 30) * 4 + 4;
            case LASER -> minutes + minutes + 6;
            case CNC -> 16 + Math.floorDiv(minutes - 1, 15) * 7;
        };
    }
    private static Outcome run(boolean baseline, Request request, AuditTrail audit, StatusReporter reporter) {
        return baseline ? new LegacyOptionalWorkflow().book(request, audit, reporter)
                : new OptionalWorkflow().book(request, audit, reporter);
    }
    public static void main(String[] args) {
        if (args.length != 1 || (!args[0].equals("baseline") && !args[0].equals("full")))
            throw new IllegalArgumentException("mode");
        boolean baseline = args[0].equals("baseline");
        for (Machine machine : Machine.values()) {
            for (int minutes = 1; minutes <= 240; minutes++)
                equal(price(machine, minutes), baseline
                        ? LegacyOptionalWorkflow.legacyCredits(machine, minutes)
                        : FeePolicies.forMachine(machine).creditsFor(minutes), "fee domain");
            for (int minutes : new int[] {0, 241}) {
                try {
                    if (baseline) LegacyOptionalWorkflow.legacyCredits(machine, minutes);
                    else FeePolicies.forMachine(machine).creditsFor(minutes);
                    throw new AssertionError("Invalid fee duration accepted");
                } catch (IllegalArgumentException error) { equal("minutes", error.getMessage(), "fee minutes"); }
            }
        }
        try {
            if (baseline) LegacyOptionalWorkflow.legacyCredits(null, 0);
            else FeePolicies.forMachine(null);
            throw new AssertionError("Missing fee machine accepted");
        } catch (IllegalArgumentException error) { equal("machine", error.getMessage(), "fee machine first"); }
        Member[] members = {null, new Member(false, false, 0), new Member(false, false, 3),
                new Member(true, false, 5), new Member(false, true, 5)};
        int rows = 0;
        for (Machine machine : Machine.values())
            for (Member member : members)
                for (int minutes : new int[] {1, machine.limitMinutes(), machine.limitMinutes() + 1})
                    for (boolean closed : new boolean[] {false, true})
                        for (boolean supervised : new boolean[] {false, true})
                            for (boolean busy : new boolean[] {false, true})
                                for (boolean absent : new boolean[] {false, true}) {
                                    Request request = new Request(machine, member, minutes, closed, supervised, busy);
                                    CoreChecks.Expected model = CoreChecks.expected(request);
                                    int credits = model.decision().allowed() ? price(machine, minutes) : 0;
                                    RecordingReporter reporter = new RecordingReporter();
                                    AuditTrail audit = new AuditTrail(); audit.note("seed");
                                    Outcome result = run(baseline, request, audit, absent ? null : reporter);
                                    equal(new Outcome(model.decision(), credits), result, "workflow outcome");
                                    equal(model.events(), audit.events(), "core audit unchanged");
                                    equal(absent ? List.of() : List.of("started",
                                            "completed:" + model.decision().reason() + ":" + credits),
                                            reporter.events, "optional reporter");
                                    rows++;
                                }
        for (boolean absent : new boolean[] {false, true}) {
            RecordingReporter reporter = new RecordingReporter();
            AuditTrail audit = new AuditTrail(); audit.note("seed");
            try {
                run(baseline, null, audit, absent ? null : reporter);
                throw new AssertionError("Missing request accepted");
            } catch (IllegalArgumentException error) { equal("request", error.getMessage(), "workflow exception"); }
            equal(List.of("seed"), audit.events(), "error audit");
            equal(absent ? List.of() : List.of("started", "failed:IllegalArgumentException:request"),
                    reporter.events, "error report order");
            RecordingReporter nullAuditReporter = new RecordingReporter();
            try {
                run(baseline, null, null, absent ? null : nullAuditReporter);
                throw new AssertionError("Missing required audit accepted");
            } catch (NullPointerException error) { equal("audit", error.getMessage(), "required audit stays required"); }
            equal(absent ? List.of() : List.of("started", "failed:NullPointerException:audit"),
                    nullAuditReporter.events, "audit failure report");
        }
        equal(720, rows, "workflow rows");
        equal(StatusReporter.none(), StatusReporter.none(), "stable Null Object");
        StatusReporter.none().started();
        StatusReporter.none().completed(new Decision(false, "guest", 0), 0);
        StatusReporter.none().failed(new IllegalStateException("example"));
        System.out.println(args[0] + " workflow-rows=" + rows + " checks=" + checks);
    }
}
