public class Main {
    record Stock(String name, int amount) {}

    static Stock parse(String line) {
        if (line == null) throw new IllegalArgumentException("Missing stock record.");
        String[] fields = line.split("\\|", -1);
        if (fields.length != 2 || fields[0].isBlank()) {
            throw new IllegalArgumentException("Expected a name and an amount.");
        }
        int amount;
        try {
            amount = Integer.parseInt(fields[1].strip());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Amount must be an integer.", error);
        }
        if (amount < 0) throw new IllegalArgumentException("Amount must be nonnegative.");
        return new Stock(fields[0].strip(), amount);
    }

    public static void main(String[] args) {
        Stock stock = parse(" flour | 12 ");
        System.out.println(stock.name() + ": " + stock.amount());
    }
}
