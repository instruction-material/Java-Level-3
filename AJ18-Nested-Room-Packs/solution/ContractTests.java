import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    public static void main(String[] args) throws Exception {
        var code = new Main.Room.Code(3);
        var north = new Main.Room("North");
        var south = new Main.Room("South");
        var first = north.new Desk(code);
        var second = south.new Desk(code);
        same("North-3", first.label());
        same("South-3", second.label());
        north.rename(" Maple ");
        same("Maple-3", first.label());
        same("South-3", second.label());
        same(3, code.value());
        try {
            new Main.Room.Code(0);
            throw new AssertionError("Zero is not a desk number");
        } catch (IllegalArgumentException error) {
            same("Desk number must be positive", error.getMessage());
        }
        System.out.println("Reference lab checks passed");
    }
}
