package checkout;
import java.util.Objects;
/** Preserved behavioral baseline: the pricing and delivery decisions are duplicated. */
public final class LegacyCheckout {
    private LegacyCheckout() {}
    public static Totals quote(Order order) {
        Objects.requireNonNull(order,"order");
        int subtotal;
        if(order.tier()==Order.Tier.MEMBER) subtotal=order.units()*180;
        else subtotal=order.units()*200;
        int shipping;
        if(order.units()==0 || order.delivery()==Order.Delivery.PICKUP) shipping=0;
        else if(order.delivery()==Order.Delivery.EXPRESS) shipping=900;
        else if(subtotal>=2000) shipping=0;
        else shipping=500;
        return new Totals(subtotal,shipping);
    }
    public static String receipt(Order order) {
        Objects.requireNonNull(order,"order");
        int subtotal;
        if(order.tier()==Order.Tier.MEMBER) subtotal=order.units()*180;
        else subtotal=order.units()*200;
        int shipping;
        if(order.units()==0 || order.delivery()==Order.Delivery.PICKUP) shipping=0;
        else if(order.delivery()==Order.Delivery.EXPRESS) shipping=900;
        else if(subtotal>=2000) shipping=0;
        else shipping=500;
        return order.tier()+"|"+order.delivery()+"|"+order.units()+"|"+subtotal+"|"+shipping+"|"+(subtotal+shipping);
    }
}
