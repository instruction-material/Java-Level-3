package supplies;

import java.util.List;

/** Runnable compatibility shell: replace its parallel-array owner in the core task. */
public final class SupplyRequest {
    private final LegacySupplyRequest legacy;
    public SupplyRequest(String destination, int service, String[] codes, int[] units) {
        legacy = new LegacySupplyRequest(destination, service, codes, units);
    }
    public static SupplyRequest fromLines(String destination, ServiceLevel service, List<RequestLine> lines) {
        // TODO core: own an ordered snapshot of the supplied immutable values.
        throw new UnsupportedOperationException("Implement typed collection ownership.");
    }
    public List<RequestLine> lines() {
        // TODO core: expose the owned unmodifiable snapshot.
        throw new UnsupportedOperationException("Implement typed collection ownership.");
    }
    public String destination() { return legacy.destination(); }
    public int serviceCode() { return legacy.serviceCode(); }
    public int dispatchMinutes() { return legacy.dispatchMinutes(); }
    public String[] itemCodes() { return legacy.itemCodes(); }
    public int[] quantities() { return legacy.quantities(); }
    public String toLegacy() { return legacy.toLegacy(); }
    @Override public boolean equals(Object other) {
        return other instanceof SupplyRequest request && legacy.equals(request.legacy);
    }
    @Override public int hashCode() { return legacy.hashCode(); }
    public SupplyRequest merged() {
        // TODO optional feature: combine duplicate codes in first-occurrence order.
        throw new UnsupportedOperationException("Implement optional merge.");
    }
}
