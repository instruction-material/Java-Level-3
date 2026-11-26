import java.util.*;

class Main {
    static String reply(String name, String activity) {
        String suggestion = switch (activity.strip().toLowerCase(Locale.ROOT)) {
            case "reading" -> "Try a short story.";
            case "drawing" -> "Try sketching a tree.";
            default -> "Tell me more about that activity.";
        };
        return "Hello, " + name.strip() + "! " + suggestion;
    }

    public static void main(String[] args) {
        var input = new Scanner(System.in);
        if (!input.hasNextLine()) return;
        String name = input.nextLine();
        if (!input.hasNextLine()) return;
        System.out.println(reply(name, input.nextLine()));
    }
}
