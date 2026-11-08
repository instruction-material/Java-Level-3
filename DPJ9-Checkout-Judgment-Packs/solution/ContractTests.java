import checkout.Checkout;
import checkout.LegacyCheckout;
import checkout.Order;
import checkout.Totals;
public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++;if(!java.util.Objects.equals(actual,expected))throw new AssertionError(actual+" != "+expected);
    }
    private static void fails(Class<? extends Throwable> type,Runnable work) {
        checks++;
        try {work.run();}catch(Throwable error){
            if(type.isInstance(error))return;
            throw new AssertionError(error);
        }
        throw new AssertionError(type.getSimpleName());
    }
    private static Totals expected(Order order) {
        int itemCents=order.tier()==Order.Tier.REGULAR?200:180;
        int amount=itemCents*order.units();
        int fee=switch(order.delivery()){
            case PICKUP -> 0;
            case EXPRESS -> order.units()==0?0:900;
            case STANDARD -> order.units()==0 || amount>=2000?0:500;
        };
        return new Totals(amount,fee);
    }
    private static void base(boolean target) {
        for(int units=0;units<=100;units++)
            for(Order.Tier tier:Order.Tier.values())
                for(Order.Delivery delivery:Order.Delivery.values()){
                    Order order=new Order(units,tier,delivery);Totals value=expected(order);
                    String receipt=tier+"|"+delivery+"|"+units+"|"+value.subtotal()+"|"+value.shipping()+"|"+value.total();
                    equal(LegacyCheckout.quote(order),value);
                    equal(LegacyCheckout.receipt(order),receipt);
                    if(target){equal(Checkout.quote(order),value);equal(Checkout.receipt(order),receipt);}
                }
        fails(IllegalArgumentException.class,()->new Order(-1,Order.Tier.REGULAR,Order.Delivery.PICKUP));
        fails(IllegalArgumentException.class,()->new Order(101,Order.Tier.REGULAR,Order.Delivery.PICKUP));
        fails(NullPointerException.class,()->new Order(1,null,Order.Delivery.PICKUP));
        fails(NullPointerException.class,()->new Order(1,Order.Tier.REGULAR,null));
        fails(IllegalArgumentException.class,()->new Totals(-1,0));
        fails(IllegalArgumentException.class,()->new Totals(0,-1));
        fails(ArithmeticException.class,()->new Totals(Integer.MAX_VALUE,1).total());
        fails(NullPointerException.class,()->LegacyCheckout.quote(null));
        fails(NullPointerException.class,()->LegacyCheckout.receipt(null));
        if(target){
            fails(NullPointerException.class,()->Checkout.quote(null));
            fails(NullPointerException.class,()->Checkout.receipt(null));
        }
    }
    private static void optional(){
        Order order=new Order(5,Order.Tier.MEMBER,Order.Delivery.STANDARD);
        int[] calls={0};
        equal(Checkout.quoteWith(order,o->{calls[0]++;return 7;}),new Totals(900,7));
        equal(calls[0],1);
        equal(Checkout.quote(order),new Totals(900,500));
        fails(NullPointerException.class,()->Checkout.quoteWith(null,o->0));
        fails(NullPointerException.class,()->Checkout.quoteWith(order,null));
        fails(IllegalArgumentException.class,()->Checkout.quoteWith(order,o->-1));
        fails(IllegalArgumentException.class,()->Checkout.quoteWith(order,o->10001));
        fails(IllegalStateException.class,()->Checkout.quoteWith(order,o->{throw new IllegalStateException("policy");}));
        equal(Checkout.quoteWith(new Order(0,Order.Tier.REGULAR,Order.Delivery.STANDARD),o->7),new Totals(0,7));
        equal(LegacyCheckout.quote(order),new Totals(900,500));
    }
    public static void main(String[] args){
        if(args.length>1 || (args.length==1 && !args[0].equals("base") && !args[0].equals("core")))throw new IllegalArgumentException("use base, core or no arguments");
        boolean baseOnly=args.length==1 && args[0].equals("base");
        base(!baseOnly);
        String label=baseOnly?"Baseline":"Core";
        if(args.length==0){optional();label="Complete";}
        System.out.println(label+" judgment contracts: "+checks+" passed");
    }
}
