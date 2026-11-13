package supplies;

import java.util.Arrays;
import java.util.StringJoiner;

/** Preserved parallel-array implementation: retain as the behavior oracle. */
public final class LegacySupplyRequest {
    private final String destination;
    private final int service;
    private final String[] codes;
    private final int[] units;
    public LegacySupplyRequest(String destination, int service, String[] codes, int[] units) {
        if (destination == null || !destination.matches("[A-Z]{1,12}"))
            throw new IllegalArgumentException("destination");
        if (service < 0 || service > 2) throw new IllegalArgumentException("service");
        if (codes == null || units == null || codes.length != units.length || codes.length > 64)
            throw new IllegalArgumentException("items");
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] == null || !codes[i].matches("[A-Z][A-Z0-9]{0,7}"))
                throw new IllegalArgumentException("code");
            if (units[i] < 1 || units[i] > 999) throw new IllegalArgumentException("units");
        }
        this.destination = destination;
        this.service = service;
        this.codes = codes.clone();
        this.units = units.clone();
    }
    public String destination() { return destination; }
    public int serviceCode() { return service; }
    public int dispatchMinutes() {
        return switch (service) { case 0 -> 180; case 1 -> 60; default -> 30; };
    }
    public String[] itemCodes() { return codes.clone(); }
    public int[] quantities() { return units.clone(); }
    public String toLegacy() {
        StringJoiner body = new StringJoiner(";");
        for (int i = 0; i < codes.length; i++) body.add(codes[i] + ":" + units[i]);
        return destination + "|" + service + "|" + body;
    }
    public static LegacySupplyRequest parse(String row) {
        try {
            if (row == null) throw new IllegalArgumentException("row");
            String[] fields = row.split("\\|", -1);
            if (fields.length != 3 || !fields[1].matches("[012]"))
                throw new IllegalArgumentException("row");
            String[] entries = fields[2].isEmpty() ? new String[0] : fields[2].split(";", -1);
            String[] codes = new String[entries.length];
            int[] units = new int[entries.length];
            for (int i = 0; i < entries.length; i++) {
                String[] pair = entries[i].split(":", -1);
                if (pair.length != 2 || !pair[1].matches("[1-9][0-9]{0,2}"))
                    throw new IllegalArgumentException("row");
                codes[i] = pair[0];
                units[i] = Integer.parseInt(pair[1]);
            }
            return new LegacySupplyRequest(fields[0], Integer.parseInt(fields[1]), codes, units);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("row");
        }
    }
    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof LegacySupplyRequest request)) return false;
        return destination.equals(request.destination) && service == request.service
            && Arrays.equals(codes, request.codes) && Arrays.equals(units, request.units);
    }
    @Override public int hashCode() {
        int result = 31 * (31 + destination.hashCode()) + service;
        for (int i = 0; i < codes.length; i++)
            result = 31 * result + 31 * codes[i].hashCode() + units[i];
        return result;
    }
}
