// Adapter: convert the foreign request and validate its response at one boundary.
package boundary;
import java.math.BigDecimal;
import java.util.Objects;

public final class PriceAdapter implements PricePort {
    private final LegacyPrice legacy;
    public PriceAdapter(LegacyPrice legacy) { this.legacy = Objects.requireNonNull(legacy, "legacy"); }
    public BigDecimal total(int units) {
        throw new UnsupportedOperationException("Implement typed legacy-price translation.");
    }
}
