package checks;

import java.util.Objects;
import commands.Parser;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input");
    }

    public static void main(String[] args) {
        var parser = new Parser();
        same(new Parser.Add("pencils", 3), parser.parse(" add   pencils    3 "));
        same(new Parser.Print(), parser.parse("PRINT"));
        same(new Parser.Quit(), parser.parse(" Quit "));
        for (String invalid : new String[]{"", "ADD pencil", "ADD pencil 0", "ADD pencil 2147483648",
                "ADD pencil two", "ADD pencil 1 extra", "PRINT item", "STOP"}) {
            rejects(() -> parser.parse(invalid));
        }
        System.out.println("AJ19 checks passed");
    }
}
