import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static HashMap<String, LinkedHashSet<String>> index(Scanner input) {
        var index = new HashMap<String, LinkedHashSet<String>>();
        String page = null;
        while (input.hasNextLine()) {
            String line = input.nextLine().strip();
            if (line.isBlank()) continue;
            if ((line.startsWith("http://") || line.startsWith("https://"))
                    && line.codePoints().noneMatch(Character::isWhitespace)) {
                page = line;
                continue;
            }
            for (String token : line.split("(?U)\\s+")) {
                if (token.isEmpty()) continue;
                if (page == null) throw new IllegalArgumentException("Tags need a page address");
                String tag = token.toLowerCase(Locale.ROOT);
                if (!index.has(tag)) index.insert(tag, new LinkedHashSet<>());
                index.get(tag).add(page);
            }
        }
        return index;
    }

    static List<String> lookup(HashMap<String, LinkedHashSet<String>> index, String query) {
        String key = query.strip().toLowerCase(Locale.ROOT);
        return index.has(key) ? List.copyOf(index.get(key)) : List.of();
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
