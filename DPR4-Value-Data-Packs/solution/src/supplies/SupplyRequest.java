package supplies;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** Value with an owned, ordered snapshot of immutable line values. */
public final class SupplyRequest {
    private final String destination;
    private final ServiceLevel service;
    private final List<RequestLine> lines;
    public SupplyRequest(String destination, int service, String[] codes, int[] units) {
        this(requireDestination(destination), ServiceLevel.fromCode(service), toLines(codes, units));
    }
    private SupplyRequest(String destination, ServiceLevel service, List<RequestLine> lines) {
        this.destination = requireDestination(destination);
        if (service == null) throw new IllegalArgumentException("service");
        if (lines == null || lines.size() > 64 || lines.stream().anyMatch(line -> line == null))
            throw new IllegalArgumentException("lines");
        this.service = service;
        this.lines = List.copyOf(lines);
    }
    private static String requireDestination(String value) {
        if (value == null || !value.matches("[A-Z]{1,12}"))
            throw new IllegalArgumentException("destination");
        return value;
    }
    private static List<RequestLine> toLines(String[] codes, int[] units) {
        if (codes == null || units == null || codes.length != units.length || codes.length > 64)
            throw new IllegalArgumentException("items");
        ArrayList<RequestLine> result = new ArrayList<>();
        for (int i = 0; i < codes.length; i++)
            result.add(new RequestLine(new ItemCode(codes[i]), units[i]));
        return result;
    }
    public static SupplyRequest fromLines(String destination, ServiceLevel service, List<RequestLine> lines) {
        return new SupplyRequest(destination, service, lines);
    }
    public String destination() { return destination; }
    public int serviceCode() { return service.code(); }
    public int dispatchMinutes() { return service.dispatchMinutes(); }
    public List<RequestLine> lines() { return lines; }
    public String[] itemCodes() {
        return lines.stream().map(line -> line.code().value()).toArray(String[]::new);
    }
    public int[] quantities() { return lines.stream().mapToInt(RequestLine::units).toArray(); }
    public String toLegacy() { return RequestCodec.encode(this); }
    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof SupplyRequest request)) return false;
        return destination.equals(request.destination) && service == request.service && lines.equals(request.lines);
    }
    @Override public int hashCode() {
        int result = 31 * (31 + destination.hashCode()) + service.code();
        for (RequestLine line : lines) result = 31 * result + line.hashCode();
        return result;
    }
    /** Optional feature: combine duplicate codes, retaining first-occurrence order. */
    public SupplyRequest merged() {
        LinkedHashMap<ItemCode, Integer> totals = new LinkedHashMap<>();
        for (RequestLine line : lines) totals.merge(line.code(), line.units(), Integer::sum);
        ArrayList<RequestLine> combined = new ArrayList<>();
        for (var entry : totals.entrySet())
            combined.add(new RequestLine(entry.getKey(), entry.getValue()));
        return fromLines(destination, service, combined);
    }
}
