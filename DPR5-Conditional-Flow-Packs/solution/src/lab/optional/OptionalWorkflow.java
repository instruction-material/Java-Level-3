package lab.optional;
import lab.*;
import lab.BookingData.*;

public final class OptionalWorkflow {
    public Outcome book(Request request, AuditTrail audit, StatusReporter reporter) {
        StatusReporter target = reporter == null ? StatusReporter.none() : reporter;
        target.started();
        try {
            Decision decision = new GatePolicy().evaluate(request, audit);
            int credits = decision.allowed()
                    ? FeePolicies.forMachine(request.machine()).creditsFor(request.minutes()) : 0;
            target.completed(decision, credits);
            return new Outcome(decision, credits);
        } catch (RuntimeException error) {
            target.failed(error);
            throw error;
        }
    }
}
