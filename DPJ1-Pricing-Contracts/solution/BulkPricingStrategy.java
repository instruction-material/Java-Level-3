public final class BulkPricingStrategy implements PricingStrategy {
    @Override
    public double priceFor(int units) {
        if (units < 0 || units > 1_000_000) {
            throw new IllegalArgumentException("units must be 0..1000000");
        }
        double unitPrice = units >= 10 ? 2.5 : units >= 5 ? 3.0 : 3.5;
        return units * unitPrice;
    }
}
