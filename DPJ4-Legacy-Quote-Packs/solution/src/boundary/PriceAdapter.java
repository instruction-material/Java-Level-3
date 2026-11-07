// Adapter: convert the foreign request and validate its response at one boundary.
package boundary;
import java.math.BigDecimal;
import java.util.Objects;

public final class PriceAdapter implements PricePort {
    private final LegacyPrice legacy;
    public PriceAdapter(LegacyPrice legacy) { this.legacy = Objects.requireNonNull(legacy, "legacy"); }
    public BigDecimal total(int units) {
        if (units < 0 || units > 1000) throw new IllegalArgumentException("units must be 0..1000");
        String result = Objects.requireNonNull(legacy.totalFor(Integer.toString(units)), "legacy total");
        if (!result.matches("USD [0-9]+\\.[0-9]{2}"))
            throw new IllegalArgumentException("legacy total must be USD with two decimals");
        return new BigDecimal(result.substring(4));
    }
}
