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
        Objects.requireNonNull(order,"order");
        int amount=subtotal(order);
        return new Totals(amount,shipping(order,amount));
    }
    public static String receipt(Order order) {
        Totals totals=quote(order);
        return order.tier()+"|"+order.delivery()+"|"+order.units()+"|"+totals.subtotal()+"|"+totals.shipping()+"|"+totals.total();
    }
    public static Totals quoteWith(Order order,ToIntFunction<Order> policy) {
        Objects.requireNonNull(order,"order");
        Objects.requireNonNull(policy,"policy");
        int fee=policy.applyAsInt(order);
        if(fee<0 || fee>10000)throw new IllegalArgumentException("policy fee must be 0..10000");
        return new Totals(subtotal(order),fee);
    }
}
