import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static List<String> readWords(Path path) throws IOException {
        String contents = Files.readString(path).strip();
        if (contents.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(contents.split("\\p{javaWhitespace}+")));
    }

    static void sort(List<String> words) {
        // TODO: Sort words by adjacent Java string comparison.
        throw new UnsupportedOperationException("Sort words by adjacent Java string comparison");
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of(args.length == 0 ? "words.txt" : args[0]);
        List<String> words = readWords(file);
        sort(words);
        System.out.println(words);
    }
}
