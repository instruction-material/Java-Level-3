import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final String EXAMPLE =
            "Harbor Bakery\n4 Wharf Road\nAlex\nBaker\n1800\nScone\nLemon\nflour, lemon\n6\n";

    static Bakery readBakery(Scanner input) {
        Bakery bakery = new Bakery(input.nextLine(), input.nextLine());
        bakery.hire(input.nextLine(), input.nextLine(), Integer.parseInt(input.nextLine().strip()));
        String productName = input.nextLine();
        String flavor = input.nextLine();
        var ingredients = Arrays.stream(input.nextLine().split(",")).map(String::strip).toList();
        int batchSize = Integer.parseInt(input.nextLine().strip());
        bakery.add(new Bakery.Product(productName, flavor, ingredients, batchSize));
        return bakery;
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
