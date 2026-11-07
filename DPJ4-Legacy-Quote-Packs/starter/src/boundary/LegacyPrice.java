package boundary;
import java.math.BigDecimal;

/** Foreign string API: the adapter owns translation to the domain's typed API. */
@FunctionalInterface
public interface LegacyPrice {
    String totalFor(String unitsText);
    final class Standard implements LegacyPrice {
        public String totalFor(String unitsText) {
            int units = Integer.parseInt(unitsText);
            if (units < 0 || units > 1000) throw new IllegalArgumentException("units must be 0..1000");
            int unitCents = units >= 10 ? 250 : units >= 5 ? 300 : 350;
            return "USD " + BigDecimal.valueOf((long) units * unitCents, 2).toPlainString();
        }
    }
}
