package lab;
import lab.BookingData.*;

public final class GatePolicy implements AdmissionPolicy {
    @Override
    public Decision evaluate(Request request, AuditTrail audit) {
        // TODO core 1: validate, use ordered guard clauses, and record exactly one decision.
        throw new UnsupportedOperationException("TODO evaluate");
    }
    private boolean blocked(Member member) {
        // TODO core 2: combine both inactive-account conditions without changing their result.
        throw new UnsupportedOperationException("TODO blocked");
    }
    private boolean ready(Request request, AuditTrail audit) {
        // TODO core 3: supervision short-circuits the observable training probe.
        throw new UnsupportedOperationException("TODO ready");
    }
    private Decision finish(boolean allowed, String reason, Request request, AuditTrail audit) {
        Decision result = new Decision(allowed, reason, allowed ? request.minutes() : 0);
        audit.decision(result);
        return result;
    }
}
