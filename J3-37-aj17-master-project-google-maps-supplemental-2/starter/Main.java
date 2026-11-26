import java.util.ArrayList;
import java.util.List;

class Main {
    record Leg(String from, String to, long miles) {}
    record Stop(String name, long totalMiles) {}

    static List<Stop> report(String origin, List<Leg> legs) {
        // TODO: Accumulate mileage through connected legs.
        throw new UnsupportedOperationException("Accumulate mileage through connected legs");
    }

    public static void main(String[] args) {
        System.out.println(report("Harbor", List.of(new Leg("Harbor", "Market", 2), new Leg("Market", "Station", 3))));
    }
}
