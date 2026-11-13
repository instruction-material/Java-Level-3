package lab;
import lab.BookingData.Decision;
import lab.BookingData.Request;

@FunctionalInterface
public interface AdmissionPolicy {
    Decision evaluate(Request request, AuditTrail audit);
}
