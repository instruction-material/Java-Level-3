public class ContractTests {
    private static int checks;
    private static void expect(int units, double expected) {
        double actual = new BulkPricingStrategy().priceFor(units);
        if (Double.compare(actual, expected) != 0) {
            throw new AssertionError(units + ": expected " + expected + ", got " + actual);
        }
        checks++;
    }
    private static void invalid(int units) {
        try {
            new BulkPricingStrategy().priceFor(units);
            throw new AssertionError("Expected IllegalArgumentException for " + units);
        } catch (IllegalArgumentException expected) {
            checks++;
        }
    }
    public static void main(String[] args) {
        int[] quantities = {0, 1, 4, 5, 6, 9, 10, 11, 1_000_000};
        double[] totals = {0, 3.5, 14, 15, 18, 27, 25, 27.5, 2_500_000};
        for (int i = 0; i < quantities.length; i++) expect(quantities[i], totals[i]);
        for (int units = 0; units <= 10_000; units++) {
            int cents = units * (units < 5 ? 350 : units < 10 ? 300 : 250);
            expect(units, cents / 100.0);
        }
        invalid(-1);
        invalid(1_000_001);
        invalid(Integer.MIN_VALUE);
        invalid(Integer.MAX_VALUE);
        System.out.println("Pricing contracts: " + checks + " passed");
    }
}
