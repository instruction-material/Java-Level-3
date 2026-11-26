import java.util.ArrayDeque;

public class Main {
    static boolean isBalanced(String text) {
        if (text == null) return false;
        var openings = new ArrayDeque<Character>();
        for (int index = 0; index < text.length(); index++) {
            char symbol = text.charAt(index);
            switch (symbol) {
                case '(', '[', '{' -> openings.push(symbol);
                case ')', ']', '}' -> {
                    if (openings.isEmpty()) return false;
                    char expected = switch (openings.pop()) {
                        case '(' -> ')';
                        case '[' -> ']';
                        default -> '}';
                    };
                    if (symbol != expected) return false;
                }
                default -> { return false; }
            }
        }
        return openings.isEmpty();
    }

    public static void main(String[] args) {
        for (String text : new String[]{"{[()]}", "([)]", "]", "(()"}) {
            System.out.println(text + " -> " + isBalanced(text));
        }
    }
}
