interface PricingStrategy {
	double priceFor(int units);
}

final class BulkPricingStrategy implements PricingStrategy {
	@Override
	public double priceFor(int units) {
		if (units >= 10) {
			return units * 2.5;
		}
		if (units >= 5) {
			return units * 3.0;
		}
		return units * 3.5;
	}
}

public class Main {
	public static void main(String[] args) {
		PricingStrategy strategy = new BulkPricingStrategy();
		System.out.println(strategy.priceFor(6));
	}
}
