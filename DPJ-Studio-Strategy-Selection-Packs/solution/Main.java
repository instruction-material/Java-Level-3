import pricing.CartPricer;
import pricing.Plan;
import pricing.legacy.LegacyPricer;

public final class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("base")) {
            LegacyPricer cart = new LegacyPricer();
            System.out.println(cart.quote(Plan.BULK, 6));
            System.out.println(cart.quote(Plan.FLAT, 6));
            System.out.println(cart.quote(Plan.MEMBER, 6));
            System.out.println(cart.receipt(Plan.BULK, 6));
        } else {
            CartPricer cart = new CartPricer();
            System.out.println(cart.quote(Plan.BULK, 6));
            System.out.println(cart.quote(Plan.FLAT, 6));
            System.out.println(cart.quote(Plan.MEMBER, 6));
            System.out.println(cart.receipt(Plan.BULK, 6));
        }
    }
}
