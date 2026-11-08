package pricing;

/** A pure pricing calculation for a validated quantity from 0 through 1000. */
@FunctionalInterface
public interface PricingRule {
    long price(int units);
}
