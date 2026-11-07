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
        Objects.requireNonNull(draft, "draft");
        OrderDraft snapshot = draft.copy();
        int units = snapshot.totalUnits();
        if (units > 1000) throw new IllegalArgumentException("total units must be 0..1000");
        return new Quote(snapshot.title(), units, prices.total(units));
    }
}
