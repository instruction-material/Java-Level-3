package dispatch;
import java.util.Objects;

/** Configuration is immutable. Queries leave the separate log unchanged. */
public final class DispatchService {
    private final int discountCents;
    private final DispatchLog log;
    private DispatchService(int discountCents, DispatchLog log) {
        this.log = Objects.requireNonNull(log, "log");
        if (discountCents < 0 || discountCents > 100)
            throw new IllegalArgumentException("discountCents");
        this.discountCents = discountCents;
    }
    public static DispatchService standard(DispatchLog log) {
        return discounted(0, log);
    }
    public static DispatchService discounted(int discountCents, DispatchLog log) {
        return new DispatchService(discountCents, log);
    }
    public Quote quote(Parcel parcel, DeliverySpeed speed) {
        Objects.requireNonNull(parcel, "parcel");
        Objects.requireNonNull(speed, "speed");
        int bands = (parcel.grams() + 499) / 500;
        return new Quote(parcel.label(), parcel.zone() * 100 + bands * 40
                + speed.surchargeCents() - discountCents);
    }
    public Quote send(Parcel parcel, DeliverySpeed speed) {
        Quote quote = quote(parcel, speed);
        log.append(quote);
        return quote;
    }
}
