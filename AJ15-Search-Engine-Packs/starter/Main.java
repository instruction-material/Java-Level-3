import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static HashMap<String, LinkedHashSet<String>> index(Scanner input) {
        // TODO: Build tag lists from page and keyword lines.
        throw new UnsupportedOperationException("Build tag lists from page and keyword lines");
    }

    static List<String> lookup(HashMap<String, LinkedHashSet<String>> index, String query) {
        // TODO: Return distinct matches for one normalized keyword.
        throw new UnsupportedOperationException("Return distinct matches for one normalized keyword");
    }

    public static void main(String[] args) throws IOException {
        Path filename = Path.of(args.length == 0 ? "corpus.txt" : args[0]);
        try (var input = new Scanner(filename, StandardCharsets.UTF_8)) {
            var index = index(input);
            if (input.ioException() != null) throw input.ioException();
            List<String> queries = args.length > 1
                    ? java.util.Arrays.asList(args).subList(1, args.length)
                    : List.of("Java", "tools", "unknown");
            for (String query : queries) {
                System.out.println(query + ": " + lookup(index, query));
            }
        }
    }
}
