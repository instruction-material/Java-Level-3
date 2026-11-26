import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        Scanner keys = new Scanner(System.in);
        Stack calc = new Stack();

        System.out.println("\nWelcome to the Stack Calculator!\n");
        System.out.println("“?” - display all numbers on the stack");
        System.out.println("“.” - pop the last number");
        System.out.println("“!” - end the program");
        System.out.println("“+” - add the last two numbers");
        System.out.println("“-” - subtract the last two numbers");
        System.out.println("“*” - multiply the last two numbers");
        System.out.println("“/” - divide the last two numbers");
        System.out.println("“%” - mod the last two numbers\n");

        while (true) {
            System.out.print("Enter a number or operator: ");
            if (!keys.hasNextLine()) {
                return;
            }
            String line = keys.nextLine().strip();
            if (line.isEmpty()) {
                continue;
            }
            for (String token : line.split("\\s+")) {
                switch (token) {
                    case "?" -> System.out.println(calc);
                    case "." -> popNumber(calc);
                    case "!" -> { return; }
                    case "+", "-", "*", "/", "%" -> apply(calc, token);
                    default -> pushNumber(calc, token);
                }
            }
        }
    }

    private static void popNumber(Stack calc) {
        if (calc.isEmpty()) {
            System.out.println("ERROR: Not enough arguments.");
            return;
        }
        System.out.println(calc.pop());
    }

    private static void apply(Stack calc, String operator) {
        if (calc.size() < 2) {
            System.out.println("ERROR: Not enough arguments.");
            return;
        }

        String rightToken = calc.pop();
        int right = Integer.parseInt(rightToken);
        int left = Integer.parseInt(calc.peek());
        if (right == 0 && (operator.equals("/") || operator.equals("%"))) {
            calc.push(rightToken);
            System.out.println(operator.equals("/") ? "ERROR: division by zero" : "ERROR: modulus by zero");
            return;
        }

        calc.pop();
        int answer = switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;
            case "%" -> left % right;
            default -> throw new IllegalArgumentException("Unknown operator " + operator);
        };
        calc.push(Integer.toString(answer));
    }

    private static void pushNumber(Stack calc, String token) {
        try {
            Integer.parseInt(token);
            calc.push(token);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: bad token");
        }
    }
}
