import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

public class ContractTests {
    private static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        var mei = new Main.Student("Mei", 400);
        var ana = new Main.Student("Ana", 390);
        var zoe = new Main.Student("Zoe", 390);
        var ray = new Main.Student("Ray", 355);
        var input = new java.util.ArrayList<>(List.of(zoe, ray, ana, mei));
        expect(List.of(mei, ana, zoe, ray), Main.rank(input));
        expect(List.of(zoe, ray, ana, mei), input);
        expect(List.of(), Main.rank(List.of()));
        expect(List.of(ana, ana), Main.rank(List.of(ana, ana)));
        if (args.length == 1 && args[0].equals("cutoff")) {
            expect(List.of(mei, ana, zoe), Main.topWithTies(input, 2));
            expect(List.of(mei, ana, zoe, ray), Main.topWithTies(input, 20));
            expect(List.of(), Main.topWithTies(input, 0));
            expect(List.of(), Main.topWithTies(input, -1));
            expect(List.of(), Main.topWithTies(List.of(), 2));
            System.out.println("Cutoff checks passed");
            return;
        }

        System.out.println("Priority queue and map checks passed");
    }
}
