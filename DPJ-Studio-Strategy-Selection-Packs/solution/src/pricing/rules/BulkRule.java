package pricing.rules;
import pricing.PricingRule;

public final class BulkRule implements PricingRule {
    @Override
    public long price(int units) {
        if (units >= 10) return units * 250L;
        if (units >= 5) return units * 300L;
        return units * 350L;
    }
}
