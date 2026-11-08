package pricing.legacy;

import java.util.Objects;
import pricing.Plan;

/** Preserved working baseline: quote and receipt duplicate the rule selection. */
public final class LegacyPricer {
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
}
