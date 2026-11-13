package lab.optional;
import lab.*;
import lab.BookingData.Machine;

public final class FeePolicies {
    private FeePolicies() {}
    public interface Policy { int creditsFor(int minutes); }
    public static Policy forMachine(Machine machine) {
        if (machine == null) throw new IllegalArgumentException("machine");
        return switch (machine) {
            case STANDARD -> new Standard();
            case LASER -> new Laser();
            case CNC -> new Cnc();
        };
    }
    private static final class Standard implements Policy {
        @Override public int creditsFor(int minutes) {
            Inputs.minutes(minutes);
            return ((minutes + 29) / 30) * 4;
        }
    }
    private static final class Laser implements Policy {
        @Override public int creditsFor(int minutes) {
            Inputs.minutes(minutes);
            return 6 + minutes * 2;
        }
    }
    private static final class Cnc implements Policy {
        @Override public int creditsFor(int minutes) {
            Inputs.minutes(minutes);
            return 9 + ((minutes + 14) / 15) * 7;
        }
    }
}
