import java.util.*;

class Main {
    static String reply(String name, String activity) {
        // TODO: Choose a reply from the activity.
        throw new UnsupportedOperationException("Choose a reply from the activity");
    }

    public static void main(String[] args) {
        var input = new Scanner(System.in);
        if (!input.hasNextLine()) return;
        String name = input.nextLine();
        if (!input.hasNextLine()) return;
        System.out.println(reply(name, input.nextLine()));
    }
}
