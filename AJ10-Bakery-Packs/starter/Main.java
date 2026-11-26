import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String EXAMPLE =
            "Harbor Bakery\n4 Wharf Road\nAlex\nBaker\n1800\nScone\nLemon\nflour, lemon\n6\n";

    static Bakery readBakery(Scanner input) {
        // TODO: Read and retain the worker and product with the shared input.
        throw new UnsupportedOperationException("Read and retain the worker and product with the shared input");
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("batches")) {
            var product = new Bakery.Product("Scone", "Lemon", java.util.List.of("flour"), 6);
            System.out.println("Batches: " + product.batchesFor(10));
            return;
        }
        try (Scanner input = args.length == 1 && args[0].equals("input")
                ? new Scanner(System.in) : new Scanner(EXAMPLE)) {
            Bakery bakery = readBakery(input);
            System.out.println(bakery.workers().getFirst().badge());
            System.out.println(bakery.products().getFirst().summary());
        }
    }
}
