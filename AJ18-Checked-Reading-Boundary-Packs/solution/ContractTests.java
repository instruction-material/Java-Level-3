import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    public static void main(String[] args) throws Exception {
        same(0, Main.parse("0"));
        same(100, Main.parse(" 100 "));
        try {
            Main.parse("2147483648");
            throw new AssertionError("Overflowing integer should fail");
        } catch (Main.ReadingError error) {
            same("Expected an integer", error.getMessage());
        }
        same(new Main.Batch(List.of(8, 12, 0), List.of(2, 4)),
                Main.collect(List.of("8", "bad", " 12 ", "101", "0")));
        same(new Main.Batch(List.of(), List.of(1, 2)), Main.collect(List.of("-1", "")));
        same(new Main.Batch(List.of(), List.of()), Main.collect(List.of()));
        System.out.println("Reference lab checks passed");
    }
}
