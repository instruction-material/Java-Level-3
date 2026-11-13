import supplies.SupplyRequest;

/** Existing client: retain this source unchanged through the refactor. */
public final class ClientCompatibility {
    private ClientCompatibility() {}
    public static String render(SupplyRequest request) {
        int total = 0;
        for (int value : request.quantities()) total += value;
        return request.destination() + " dispatch=" + request.dispatchMinutes()
            + " lines=" + request.itemCodes().length + " units=" + total;
    }
}
