package lab;
import lab.BookingData.*;

public final class CoreDemo {
    private CoreDemo() {}
    public static void main(String[] args) {
        if (args.length > 1 || (args.length == 1 && !args[0].equals("baseline")
                && !args[0].equals("core"))) throw new IllegalArgumentException("mode");
        AdmissionPolicy policy = args.length == 0 || args[0].equals("baseline")
                ? new LegacyGate() : new GatePolicy();
        AuditTrail first = new AuditTrail();
        System.out.println(BookingClient.describe(policy,
                new Request(Machine.LASER, new Member(false, false, 2), 60,
                        false, false, false), first));
        System.out.println(first.events());
        AuditTrail second = new AuditTrail();
        System.out.println(BookingClient.describe(policy,
                new Request(Machine.CNC, new Member(false, false, 0), 20,
                        false, true, true), second));
        System.out.println(second.events());
    }
}
