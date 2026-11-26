import java.util.ArrayList;
import java.util.List;

class Main {
    record Leg(String from, String to, long miles) {}
    record Stop(String name, long totalMiles) {}

    static List<Stop> report(String origin, List<Leg> legs) {
        var stops = new ArrayList<Stop>();
        long total = 0;
        String current = origin;
        stops.add(new Stop(origin, 0));
        for (Leg leg : legs) {
            if (!current.equals(leg.from()) || leg.miles() < 0 || leg.miles() > Long.MAX_VALUE - total) {
                throw new IllegalArgumentException("Unusable leg from " + leg.from());
            }
            total += leg.miles();
            current = leg.to();
            stops.add(new Stop(current, total));
        }
        return List.copyOf(stops);
    }

    public static void main(String[] args) {
        System.out.println(report("Harbor", List.of(new Leg("Harbor", "Market", 2), new Leg("Market", "Station", 3))));
    }
}
