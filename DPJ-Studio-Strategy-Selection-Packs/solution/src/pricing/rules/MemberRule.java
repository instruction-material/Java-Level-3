package pricing.rules;
import pricing.PricingRule;

public final class MemberRule implements PricingRule {
    private final PricingRule bulk = new BulkRule();
    @Override
    public long price(int units) {
        return bulk.price(units) * 90 / 100;
    }
}
