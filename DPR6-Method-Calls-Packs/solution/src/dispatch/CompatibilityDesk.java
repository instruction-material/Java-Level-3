package dispatch;
import java.util.List;

/** Compatibility boundary retains the original constructor, setter and sentinel results. */
public final class CompatibilityDesk {
    private final DispatchLog log = new DispatchLog();
    private DispatchService service;
    public CompatibilityDesk(int discountCents) { setDiscount(discountCents); }
    public void setDiscount(int discountCents) {
        if (discountCents < 0 || discountCents > 100)
            throw new IllegalArgumentException("discountCents");
        service = DispatchService.discounted(discountCents, log);
    }
    public int price(String label, int grams, int zone, boolean express, String unused) {
        Parcel parcel;
        try { parcel = new Parcel(label, grams, zone); }
        catch (IllegalArgumentException | NullPointerException invalid) { return -1; }
        return service.send(parcel, express ? DeliverySpeed.EXPRESS : DeliverySpeed.STANDARD).cents();
    }
    public Quote find(String label) {
        if (label == null || label.isBlank()) return null;
        return log.find(label).orElse(null);
    }
    public int totalCents() { return log.totalCents(); }
    public List<Quote> history() { return log.snapshot(); }
}
