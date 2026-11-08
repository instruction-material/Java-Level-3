package checkout;
import java.util.Objects;
public record Order(int units,Tier tier,Delivery delivery) {
    public enum Tier { REGULAR, MEMBER }
    public enum Delivery { PICKUP, STANDARD, EXPRESS }
    public Order {
        if (units<0 || units>100) throw new IllegalArgumentException("units must be 0..100");
        Objects.requireNonNull(tier,"tier");Objects.requireNonNull(delivery,"delivery");
    }
}
