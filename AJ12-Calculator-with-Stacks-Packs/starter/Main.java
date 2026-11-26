import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Main {
    static boolean apply(Deque<Integer> values, String token) {
        // TODO: Apply one token without losing operands on failure.
        throw new UnsupportedOperationException("Apply one token without losing operands on failure");
    }

    public static void main(String[] args) {
        var values = new ArrayDeque<Integer>();
        var tokens = List.of("12", "3", "-", "2", "*", "0", "/");
        for (String token : tokens) {
            System.out.println(token + " -> " + apply(values, token) + " " + values);
        }
    }
}
