import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Main {
    static boolean apply(Deque<Integer> values, String token) {
        switch (token) {
            case "+", "-", "*", "/", "%" -> {
                if (values.size() < 2) return false;
                int right = values.getLast();
                if (right == 0 && (token.equals("/") || token.equals("%"))) return false;
                values.removeLast();
                int left = values.removeLast();
                int result = switch (token) {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    case "/" -> left / right;
                    default -> left % right;
                };
                values.addLast(result);
                return true;
            }
            default -> {
                try {
                    values.addLast(Integer.parseInt(token));
                    return true;
                } catch (NumberFormatException invalid) {
                    return false;
                }
            }
        }
    }

    public static void main(String[] args) {
        var values = new ArrayDeque<Integer>();
        var tokens = List.of("12", "3", "-", "2", "*", "0", "/");
        for (String token : tokens) {
            System.out.println(token + " -> " + apply(values, token) + " " + values);
        }
    }
}
