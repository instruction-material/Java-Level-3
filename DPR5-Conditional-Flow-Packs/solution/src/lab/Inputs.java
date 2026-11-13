package lab;
import java.util.Objects;
import lab.BookingData.Request;

/** Mandatory public argument validation, active with -ea and -da. */
public final class Inputs {
    private Inputs() {}
    public static void minutes(int minutes) {
        if (minutes < 1 || minutes > 240) throw new IllegalArgumentException("minutes");
    }
    public static void validate(Request request, AuditTrail audit) {
        Objects.requireNonNull(audit, "audit");
        if (request == null) throw new IllegalArgumentException("request");
        if (request.machine() == null) throw new IllegalArgumentException("machine");
        minutes(request.minutes());
        if (request.member() != null && (request.member().trainingLevel() < 0
                || request.member().trainingLevel() > 5))
            throw new IllegalArgumentException("trainingLevel");
    }
}
