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
        return new ArrayList<>(Arrays.asList(contents.split("\\s+")));
    }

    static void sort(List<String> words) {
        for (int end = words.size() - 1; end > 0; end--) {
            boolean swapped = false;
            for (int index = 0; index < end; index++) {
                String left = words.get(index);
                String right = words.get(index + 1);
                if (left.compareTo(right) > 0) {
                    words.set(index, right);
                    words.set(index + 1, left);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of(args.length == 0 ? "words.txt" : args[0]);
        List<String> words = readWords(file);
        sort(words);
        System.out.println(words);
    }
}
