import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<String> trace(int size) {
        if (size < 0) throw new IllegalArgumentException("Size must be nonnegative.");
        List<String> output = new ArrayList<>();
        visit(0, size, output);
        return output;
    }

    static void visit(int start, int end, List<String> output) {
        // TODO: Record the range before recursing into its halves.
        throw new UnsupportedOperationException("Record the range before recursing into its halves");
    }

    public static void main(String[] args) {
        System.out.println(String.join(" ", trace(3)));
    }
}
