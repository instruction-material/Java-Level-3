package pricing;

import java.util.Map;
import java.util.Objects;
import pricing.rules.BulkRule;
import pricing.rules.FlatRule;
import pricing.rules.MemberRule;

/** Stable client operations delegate calculation to one selected rule. */
public final class CartPricer {
    private final Map<Plan, PricingRule> rules = Map.of(
        Plan.BULK, new BulkRule(),
        Plan.FLAT, new FlatRule(),
        Plan.MEMBER, new MemberRule()
    );

    private static void validUnits(int units) {
        if (units < 0 || units > 1000) throw new IllegalArgumentException("units");
    }

    public long quote(Plan plan, int units) {
        Objects.requireNonNull(plan, "plan");
        validUnits(units);
        return rules.get(plan).price(units);
    }

    public String receipt(Plan plan, int units) {
        return plan.name() + "|" + units + "|" + quote(plan, units);
    }

    public long priceUsing(int units, PricingRule rule) {
        validUnits(units);
        long result = Objects.requireNonNull(rule, "rule").price(units);
        if (result < 0) throw new IllegalArgumentException("negative price");
        return result;
    }
}
