import java.util.ArrayList;
import java.util.List;

final class Bakery {
    private final String name;
    private final String address;
    private final List<Worker> workers = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();

    Bakery(String name, String address) {
        this.name = name;
        this.address = address;
    }

    String address() {
        return address;
    }

    void hire(String name, String role, int hourlyCents) {
        workers.add(new Worker(name, role, hourlyCents));
    }

    void add(Product product) {
        products.add(product);
    }

    List<Worker> workers() {
        return List.copyOf(workers);
    }

    List<Product> products() {
        return List.copyOf(products);
    }

    final class Worker {
        private final String name;
        private final String role;
        private final int hourlyCents;

        Worker(String name, String role, int hourlyCents) {
            if (hourlyCents < 0) throw new IllegalArgumentException("Negative hourly pay.");
            this.name = name;
            this.role = role;
            this.hourlyCents = hourlyCents;
        }

        String badge() {
            // TODO: Format a badge using the enclosing bakery.
            throw new UnsupportedOperationException("Format a badge using the enclosing bakery");
        }

        int hourlyCents() {
            return hourlyCents;
        }
    }

    record Product(String name, String flavor, List<String> ingredients, int batchSize) {
        Product {
            if (batchSize <= 0) throw new IllegalArgumentException("Batch size must be positive.");
            ingredients = List.copyOf(ingredients);
        }

        String summary() {
            return name + " (" + flavor + ") | " + String.join(", ", ingredients);
        }

        int batchesFor(int requested) {
            // TODO: Round the requested quantity up to whole batches.
            throw new UnsupportedOperationException("Round the requested quantity up to whole batches");
        }
    }
}
