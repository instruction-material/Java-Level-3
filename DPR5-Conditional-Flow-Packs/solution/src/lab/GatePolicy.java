package lab;
import lab.BookingData.*;

public final class GatePolicy implements AdmissionPolicy {
    @Override
    public Decision evaluate(Request request, AuditTrail audit) {
        Inputs.validate(request, audit);
        if (request.member() == null) return finish(false, "guest", request, audit);
        if (blocked(request.member())) return finish(false, "inactive", request, audit);
        if (request.closed()) return finish(false, "closed", request, audit);
        if (request.minutes() > request.machine().limitMinutes())
            return finish(false, "too-long", request, audit);
        if (!ready(request, audit)) return finish(false, "training", request, audit);
        if (!audit.available(request.busy())) return finish(false, "busy", request, audit);
        return finish(true, "approved", request, audit);
    }
    private boolean blocked(Member member) {
        return member.suspended() || member.revoked();
    }
    private boolean ready(Request request, AuditTrail audit) {
        return request.supervised() || audit.training(request.member().trainingLevel(),
                request.machine().requiredTraining());
    }
    private Decision finish(boolean allowed, String reason, Request request, AuditTrail audit) {
        Decision result = new Decision(allowed, reason, allowed ? request.minutes() : 0);
        audit.decision(result);
        return result;
    }
}
