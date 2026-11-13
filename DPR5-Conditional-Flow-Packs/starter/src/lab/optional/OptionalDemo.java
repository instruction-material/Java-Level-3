package lab.optional;
import lab.*;
import lab.BookingData.*;

public final class OptionalDemo {
    private OptionalDemo() {}
    public static void main(String[] args) {
        Outcome result = new OptionalWorkflow().book(
                new Request(Machine.LASER, new Member(false, false, 2), 60,
                        false, false, false), new AuditTrail(), null);
        System.out.println(result.decision().reason() + " credits=" + result.credits());
    }
}
