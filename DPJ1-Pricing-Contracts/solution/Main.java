public class Main {
    public static void main(String[] args) {
        PricingStrategy strategy = new BulkPricingStrategy();
        System.out.println(strategy.priceFor(6));
    }
}
