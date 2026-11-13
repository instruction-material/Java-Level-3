package lab;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lab.BookingData.*;

/** Explicit checks stay active even when Java assertions are disabled. */
public final class CoreChecks {
    private static int checks;
    private CoreChecks() {}
    public record Expected(Decision decision, List<String> events) {}
    public static void equal(Object expected, Object actual, String label) {
        checks++;
        if (!Objects.equals(expected, actual))
            throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
    }
    public static Expected expected(Request r) {
        List<String> events = new ArrayList<>();
        events.add("seed");
        String reason = null;
        Member m = r.member();
        if (m == null) reason = "guest";
        else if (m.suspended() || m.revoked()) reason = "inactive";
        else if (r.closed()) reason = "closed";
        else if (r.minutes() > r.machine().limitMinutes()) reason = "too-long";
        else {
            if (!r.supervised()) {
                events.add("training:" + m.trainingLevel() + ">=" + r.machine().requiredTraining());
                if (m.trainingLevel() < r.machine().requiredTraining()) reason = "training";
            }
            if (reason == null) {
                events.add("availability:busy=" + r.busy());
                if (r.busy()) reason = "busy";
            }
        }
        if (reason == null) reason = "approved";
        boolean allowed = reason.equals("approved");
        events.add("decision:" + reason);
        return new Expected(new Decision(allowed, reason, allowed ? r.minutes() : 0),
                List.copyOf(events));
    }
    public static String errorKey(RuntimeException error) {
        return error.getClass().getSimpleName() + ":" + error.getMessage();
    }
    private static void invalid(AdmissionPolicy policy, Request request, AuditTrail audit, String key) {
        try {
            policy.evaluate(request, audit);
            throw new AssertionError("Expected " + key);
        } catch (RuntimeException error) {
            equal(key, errorKey(error), "error class/message");
        }
        if (audit != null) equal(List.of("seed"), audit.events(), "failure preserves audit");
    }
    private static AuditTrail seeded() {
        AuditTrail audit = new AuditTrail();
        audit.note("seed");
        return audit;
    }
    public static void validation(AdmissionPolicy policy) {
        Member active = new Member(false, false, 2);
        invalid(policy, null, null, "NullPointerException:audit");
        invalid(policy, new Request(null, active, 0, false, false, false), null, "NullPointerException:audit");
        invalid(policy, null, seeded(), "IllegalArgumentException:request");
        invalid(policy, new Request(null, new Member(false, false, -1), 0, false, false, false),
                seeded(), "IllegalArgumentException:machine");
        invalid(policy, new Request(Machine.LASER, new Member(false, false, -1), 0, false, false, false),
                seeded(), "IllegalArgumentException:minutes");
        invalid(policy, new Request(Machine.LASER, null, 241, true, true, true),
                seeded(), "IllegalArgumentException:minutes");
        for (int level : new int[] {-1, 6})
            invalid(policy, new Request(Machine.LASER, new Member(true, true, level), 1, true, true, true),
                    seeded(), "IllegalArgumentException:trainingLevel");
    }
    private static void assertionMode() {
        boolean enabled = BookingClient.class.desiredAssertionStatus();
        AdmissionPolicy broken = (request, audit) -> new Decision(false, "guest", 99);
        try {
            String result = BookingClient.describe(broken, null, null);
            if (enabled) throw new IllegalStateException("Internal assertion did not run");
            equal("guest:99", result, "disabled assertion demonstration");
        } catch (AssertionError error) {
            if (!enabled) throw error;
            equal("denied minutes", error.getMessage(), "internal invariant message");
        }
        validation(new LegacyGate());
        System.out.println("assertions=" + (enabled ? "on" : "off") + " public-validation=active");
    }
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("mode");
        if (args[0].equals("assertions")) { assertionMode(); return; }
        AdmissionPolicy policy = switch (args[0]) {
            case "baseline" -> new LegacyGate();
            case "core" -> new GatePolicy();
            default -> throw new IllegalArgumentException("mode");
        };
        List<Member> members = new ArrayList<>();
        members.add(null);
        for (int level = 0; level <= 5; level++)
            for (boolean suspended : new boolean[] {false, true})
                for (boolean revoked : new boolean[] {false, true})
                    members.add(new Member(suspended, revoked, level));
        int rows = 0;
        for (Machine machine : Machine.values())
            for (Member member : members)
                for (int minutes = 1; minutes <= 240; minutes++)
                    for (boolean closed : new boolean[] {false, true})
                        for (boolean supervised : new boolean[] {false, true})
                            for (boolean busy : new boolean[] {false, true}) {
                                Request request = new Request(machine, member, minutes, closed, supervised, busy);
                                Expected model = expected(request);
                                AuditTrail audit = seeded();
                                Decision actual = policy.evaluate(request, audit);
                                equal(model.decision(), actual, "decision row " + rows);
                                equal(model.events(), audit.events(), "probe/decision trace row " + rows);
                                AuditTrail clientAudit = seeded();
                                equal(actual.reason() + ":" + actual.grantedMinutes(),
                                        BookingClient.describe(policy, request, clientAudit), "stable client");
                                equal(model.events(), clientAudit.events(), "stable client trace");
                                rows++;
                            }
        equal(144000, rows, "finite input rows");
        validation(policy);
        Request repeated = new Request(Machine.CNC, new Member(false, false, 3), 45, false, false, false);
        AuditTrail reused = new AuditTrail();
        policy.evaluate(repeated, reused);
        List<String> first = reused.events();
        policy.evaluate(repeated, reused);
        List<String> doubled = new ArrayList<>(first);
        doubled.addAll(first);
        equal(doubled, reused.events(), "reused audit appends");
        try { first.add("mutate"); throw new AssertionError("Mutable event snapshot"); }
        catch (UnsupportedOperationException expected) { checks++; }
        System.out.println(args[0] + " rows=" + rows + " checks=" + checks);
    }
}
