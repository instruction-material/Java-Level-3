package pricing;

import java.util.Objects;

/** Runnable core starter. Refactor duplicated decisions while preserving this contract. */
public final class CartPricer {
    public long quote(Plan plan, int units) {
        Objects.requireNonNull(plan, "plan");
        if (units < 0 || units > 1000) throw new IllegalArgumentException("units");
        long total;
        if (plan == Plan.FLAT) {
            total = units * 400L;
        } else {
            if (units >= 10) total = units * 250L;
            else if (units >= 5) total = units * 300L;
            else total = units * 350L;
            if (plan == Plan.MEMBER) total = total * 90 / 100;
        }
        return total;
    }

    public String receipt(Plan plan, int units) {
        Objects.requireNonNull(plan, "plan");
        if (units < 0 || units > 1000) throw new IllegalArgumentException("units");
        long total;
        if (plan == Plan.FLAT) {
            total = units * 400L;
        } else {
            if (units >= 10) total = units * 250L;
            else if (units >= 5) total = units * 300L;
            else total = units * 350L;
            if (plan == Plan.MEMBER) total = total * 90 / 100;
        }
        return plan.name() + "|" + units + "|" + total;
    }

    /** Optional extension. Core behavior is independent of this method. */
    public long priceUsing(int units, PricingRule rule) {
        throw new UnsupportedOperationException("Implement optional rule injection.");
    }
}
