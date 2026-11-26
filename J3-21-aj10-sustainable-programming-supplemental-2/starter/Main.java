public class Main {
    record Stock(String name, int amount) {}

    static Stock parse(String line) {
        // TODO: Convert and validate a single stock record.
        throw new UnsupportedOperationException("Convert and validate a single stock record");
    }

    public static void main(String[] args) {
        Stock stock = parse(" flour | 12 ");
        System.out.println(stock.name() + ": " + stock.amount());
    }
}
