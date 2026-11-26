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
        // TODO: Record the latest count for each normalized SKU.
        throw new UnsupportedOperationException("Record the latest count for each normalized SKU");
    }

    static OptionalInt lookup(Map<String, Integer> catalog, String query) {
        // TODO: Look up a normalized SKU while preserving missing versus zero.
        throw new UnsupportedOperationException("Look up a normalized SKU while preserving missing versus zero");
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
