import checkout.Checkout;
import checkout.LegacyCheckout;
import checkout.Order;
public class Main {
    public static void main(String[] args) {
        if(args.length>1 || (args.length==1 && !args[0].equals("base"))) throw new IllegalArgumentException("use base or no arguments");
        Order regular=new Order(10,Order.Tier.REGULAR,Order.Delivery.STANDARD);
        Order member=new Order(10,Order.Tier.MEMBER,Order.Delivery.EXPRESS);
        System.out.println(LegacyCheckout.receipt(regular));
        if(args.length==0)System.out.println(Checkout.receipt(regular));
        System.out.println((args.length==0?Checkout.quote(member):LegacyCheckout.quote(member)).total());
    }
}
