package pricing.rules;
import pricing.PricingRule;

public final class FlatRule implements PricingRule {
    @Override
    public long price(int units) {
        return units * 400L;
    }
}
