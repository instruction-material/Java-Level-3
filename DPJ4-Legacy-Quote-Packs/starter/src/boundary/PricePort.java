package boundary;
import java.math.BigDecimal;
@FunctionalInterface
public interface PricePort {
    /** Total for 0..1000 units, non-null/nonnegative USD with scale exactly two. */
    BigDecimal total(int units);
}
