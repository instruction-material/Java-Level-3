package lab;
import lab.BookingData.*;

/** Nested behavior oracle. Preserve this file during the exercise. */
public final class LegacyGate implements AdmissionPolicy {
    @Override
    public Decision evaluate(Request request, AuditTrail audit) {
        Inputs.validate(request, audit);
        boolean accepted = false;
        String reason = "guest";
        if (request.member() != null) {
            if (!request.member().suspended()) {
                if (!request.member().revoked()) {
                    if (!request.closed()) {
                        if (request.minutes() <= request.machine().limitMinutes()) {
                            boolean ready = false;
                            if (request.supervised()) {
                                ready = true;
                            } else {
                                if (audit.training(request.member().trainingLevel(),
                                        request.machine().requiredTraining())) {
                                    ready = true;
                                }
                            }
                            if (ready) {
                                if (audit.available(request.busy())) {
                                    accepted = true;
                                    reason = "approved";
                                } else {
                                    reason = "busy";
                                }
                            } else {
                                reason = "training";
                            }
                        } else {
                            reason = "too-long";
                        }
                    } else {
                        reason = "closed";
                    }
                } else {
                    reason = "inactive";
                }
            } else {
                reason = "inactive";
            }
        }
        Decision result = new Decision(accepted, reason, accepted ? request.minutes() : 0);
        audit.decision(result);
        return result;
    }
}
