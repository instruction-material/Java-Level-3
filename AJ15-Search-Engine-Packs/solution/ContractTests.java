import java.util.List;
import java.util.Objects;

public class ContractTests {
    static void expect(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    public static void main(String[] args) {
        try (var lines = new java.util.Scanner("https://example.org/a\n  Java\tJAVA\n\n\n"
                + "https://example.org/b\njava other\nhttps://example.org/c\nSOLO\n")) {
            var result = Main.index(lines);
            expect(List.of("https://example.org/a","https://example.org/b"), Main.lookup(result, " JaVa "));
            expect(List.of("https://example.org/c"), Main.lookup(result, "solo"));
            expect(List.of(), Main.lookup(result, "missing"));
        }
        try (var lines = new java.util.Scanner("")) {
            expect(List.of(), Main.lookup(Main.index(lines), "java"));
        }
        try (var invalid = new java.util.Scanner("orphan tag\n")) {
            try {
                Main.index(invalid);
                throw new AssertionError("Accepted tags before any address");
            } catch (IllegalArgumentException expected) {
                // Malformed input is rejected.
            }
        }

        System.out.println("Hash table checks passed");
    }
}
