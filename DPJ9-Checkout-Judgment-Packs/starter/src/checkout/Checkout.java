package checkout;
import java.util.Objects;
import java.util.function.ToIntFunction;
/** The reference centralizes stable rules; no new policy hierarchy is required for core. */
public final class Checkout {
    private Checkout() {}
    private static int subtotal(Order order) {
        return order.units()*(order.tier()==Order.Tier.MEMBER?180:200);
    }
    private static int shipping(Order order,int subtotal) {
        if(order.units()==0 || order.delivery()==Order.Delivery.PICKUP)return 0;
        if(order.delivery()==Order.Delivery.EXPRESS)return 900;
        return subtotal>=2000?0:500;
    }
    public static Totals quote(Order order) {
        throw new UnsupportedOperationException("Implement the behavior-preserving quote.");
    }
    public static String receipt(Order order) {
        throw new UnsupportedOperationException("Implement the behavior-preserving receipt.");
    }
    public static Totals quoteWith(Order order,ToIntFunction<Order> policy) {
        throw new UnsupportedOperationException("Implement optional injected shipping.");
    }
}
