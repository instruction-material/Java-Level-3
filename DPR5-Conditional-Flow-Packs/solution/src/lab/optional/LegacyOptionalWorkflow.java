package lab.optional;
import lab.*;
import lab.BookingData.*;

/** Optional oracle: branch-specific fees and repeated absent-reporter checks. */
public final class LegacyOptionalWorkflow {
    public Outcome book(Request request, AuditTrail audit, StatusReporter reporter) {
        if (reporter != null) reporter.started();
        try {
            Decision decision = new LegacyGate().evaluate(request, audit);
            int credits = decision.allowed() ? legacyCredits(request.machine(), request.minutes()) : 0;
            if (reporter != null) reporter.completed(decision, credits);
            return new Outcome(decision, credits);
        } catch (RuntimeException error) {
            if (reporter != null) reporter.failed(error);
            throw error;
        }
    }
    public static int legacyCredits(Machine machine, int minutes) {
        if (machine == null) throw new IllegalArgumentException("machine");
        Inputs.minutes(minutes);
        if (machine == Machine.STANDARD) return ((minutes + 29) / 30) * 4;
        if (machine == Machine.LASER) return 6 + minutes * 2;
        return 9 + ((minutes + 14) / 15) * 7;
    }
}
