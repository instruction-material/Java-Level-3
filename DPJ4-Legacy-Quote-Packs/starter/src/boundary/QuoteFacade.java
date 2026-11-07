// Facade: snapshot, validate quantity, obtain one typed price, return a value.
package boundary;
import java.math.BigDecimal;
import java.util.Objects;

public final class QuoteFacade {
    private final PricePort prices;
    public QuoteFacade(PricePort prices) { this.prices = Objects.requireNonNull(prices, "prices"); }
    public record Quote(String title, int units, BigDecimal total) {
        public Quote {
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(total, "total");
            if (title.isBlank() || title.contains("\n") || title.contains("\r") ||
                units < 0 || units > 1000 || total.signum() < 0 || total.scale() != 2)
                throw new IllegalArgumentException("invalid quote");
        }
        public String render() { return title + ": " + units + " units = USD " + total.toPlainString() + "\n"; }
    }
    public Quote quote(OrderDraft draft) {
        throw new UnsupportedOperationException("Implement the snapshot and quote workflow.");
    }
}
