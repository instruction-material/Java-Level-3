interface PricingStrategy {
	double priceFor(int units);
}

final class BulkPricingStrategy implements PricingStrategy {
	@Override
	public double priceFor(int units) {
		// TODO: apply a smarter pricing strategy.
		return units;
	}
}

public class Main {
	public static void main(String[] args) {
		PricingStrategy strategy = new BulkPricingStrategy();
		System.out.println(strategy.priceFor(6));
	}
}
