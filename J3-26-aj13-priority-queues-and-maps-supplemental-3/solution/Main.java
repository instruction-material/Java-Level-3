import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalInt;

public class Main {
    record Update(String sku, int quantity) {}

    static String key(String sku) {
        String normalized = sku.strip().toLowerCase(Locale.ROOT);
        if (normalized.isEmpty()) throw new IllegalArgumentException("Missing SKU");
        return normalized;
    }

    static Map<String, Integer> catalog(List<Update> updates) {
        var catalog = new HashMap<String, Integer>();
        for (var update : updates) {
            if (update.quantity() < 0) throw new IllegalArgumentException("Negative quantity");
            catalog.put(key(update.sku()), update.quantity());
        }
        return catalog;
    }

    static OptionalInt lookup(Map<String, Integer> catalog, String query) {
        Integer count = catalog.get(key(query));
        return count == null ? OptionalInt.empty() : OptionalInt.of(count);
    }

    public static void main(String[] args) {
        var inventory = catalog(List.of(new Update("Lamp", 4), new Update("Lamp ", 0),
                new Update("Bulb", 7)));
        for (var sku : List.of(" LAMP ", "bulb", "Cable")) {
            var count = lookup(inventory, sku);
            System.out.println(sku.strip() + ": "
                    + (count.isPresent() ? Integer.toString(count.getAsInt()) : "missing"));
        }
    }
}
