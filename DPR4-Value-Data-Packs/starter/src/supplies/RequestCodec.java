package supplies;

/** TODO core: replace this baseline adapter with one typed-data conversion boundary. */
public final class RequestCodec {
    private RequestCodec() {}
    public static SupplyRequest decode(String row) {
        LegacySupplyRequest old = LegacySupplyRequest.parse(row);
        return new SupplyRequest(old.destination(), old.serviceCode(), old.itemCodes(), old.quantities());
    }
    public static String encode(SupplyRequest request) {
        if (request == null) throw new IllegalArgumentException("request");
        return request.toLegacy();
    }
}
