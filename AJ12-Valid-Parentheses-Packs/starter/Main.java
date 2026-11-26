import java.util.ArrayDeque;

public class Main {
    static boolean isBalanced(String text) {
        // TODO: Match closing symbols to the most recent opener.
        throw new UnsupportedOperationException("Match closing symbols to the most recent opener");
    }

    public static void main(String[] args) {
        for (String text : new String[]{"{[()]}", "([)]", "]", "(()"}) {
            System.out.println(text + " -> " + isBalanced(text));
        }
    }
}
