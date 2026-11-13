package lab;
import lab.BookingData.*;

/** A stable caller: a refactor must preserve this API and result. */
public final class BookingClient {
    private BookingClient() {}
    public static String describe(AdmissionPolicy policy, Request request, AuditTrail audit) {
        Decision result = policy.evaluate(request, audit);
        // An internal postcondition; Inputs owns always-active public validation.
        assert result.allowed() || result.grantedMinutes() == 0 : "denied minutes";
        return result.reason() + ":" + result.grantedMinutes();
    }
}
