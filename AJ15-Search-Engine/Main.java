import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class Main {
    private record Index(HashMap<String, LinkedHashSet<String>> terms, int pages) {}

    public static void main(String[] args) {
        try (var console = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println("\nWelcome to the Juni Search Engine!");
            System.out.print("Please enter a file name: ");
            if (!console.hasNextLine()) return;

            Index index;
            try (var corpus = new Scanner(Path.of(console.nextLine().strip()), StandardCharsets.UTF_8)) {
                index = load(corpus);
                if (corpus.ioException() != null) throw corpus.ioException();
            } catch (IOException error) {
                System.err.println("ERROR: could not read file.");
                return;
            } catch (IllegalArgumentException error) {
                System.err.println("ERROR: " + error.getMessage());
                return;
            }
            System.out.printf("Successfully loaded %d urls and %d keywords.%n", index.pages(), index.terms().size());

            while (console.hasNextLine()) {
                String word = console.nextLine().strip().toLowerCase(Locale.ROOT);
                if (word.isEmpty()) continue;

                System.out.println("\nSearch: " + word + "\nRESULTS:");
                List<String> urls = index.terms().has(word)
                        ? List.copyOf(index.terms().get(word)) : List.of();
                for (int i = 0; i < urls.size(); i++) {
                    System.out.printf("%d: %s%n", i + 1, urls.get(i));
                }
                System.out.printf("%d search results.%n", urls.size());
            }
        }
    }

    private static Index load(Scanner corpus) {
        var terms = new HashMap<String, LinkedHashSet<String>>();
        var indexedPages = new LinkedHashSet<String>();
        String currentUrl = null;
        while (corpus.hasNextLine()) {
            String line = corpus.nextLine().strip();
            if (line.isEmpty()) continue;
            if ((line.startsWith("https://") || line.startsWith("http://"))
                    && line.codePoints().noneMatch(Character::isWhitespace)) {
                currentUrl = line;
                continue;
            }
            if (currentUrl == null) {
                throw new IllegalArgumentException("Keyword line before a page address");
            }
            for (String token : line.split("(?U)\\s+")) {
                String word = token.toLowerCase(Locale.ROOT);
                if (!terms.has(word)) terms.insert(word, new LinkedHashSet<>());
                terms.get(word).add(currentUrl);
            }
            indexedPages.add(currentUrl);
        }
        return new Index(terms, indexedPages.size());
    }
}
