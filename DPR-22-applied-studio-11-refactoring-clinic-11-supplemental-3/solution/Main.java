// Describe a pricing rule that can vary by strategy implementation
interface PricingStrategy {
    /**
	 * @brief Calculate the total price for a number of units
	 *
	 * @param units Number of units being purchased
	 *
	 * @return Total price for the purchase
	 */
    double priceFor(int units);
}

// Apply lower prices as the order size increases
final class BulkPricingStrategy implements PricingStrategy {
    private static final int LARGE_ORDER_UNITS = 10;
    private static final int MEDIUM_ORDER_UNITS = 5;
    private static final double LARGE_ORDER_UNIT_PRICE = 2.5;
    private static final double MEDIUM_ORDER_UNIT_PRICE = 3.0;
    private static final double SMALL_ORDER_UNIT_PRICE = 3.5;

    @Override
    public double priceFor(int units) {
        // Use the best unit price for large orders
        if (units >= LARGE_ORDER_UNITS) {
            return units * LARGE_ORDER_UNIT_PRICE;
        }

        // Use the middle unit price for medium orders
        if (units >= MEDIUM_ORDER_UNITS) {
            return units * MEDIUM_ORDER_UNIT_PRICE;
        }

        return units * SMALL_ORDER_UNIT_PRICE;
    }
}

public class Main {
    private static final int SAMPLE_UNITS = 6;

    /**
	 * @brief Print the sample bulk price
	 *
	 * @param args Command-line arguments
	 */
    public static void main(String[] args) {
        PricingStrategy strategy = new BulkPricingStrategy();
        System.out.println(strategy.priceFor(SAMPLE_UNITS));
    }
}
