import java.util.*;

class Main {
    static String story(String animal, String verb, String adjective, String noun) {
        // TODO: Assemble the story from four lines.
        throw new UnsupportedOperationException("Assemble the story from four lines");
    }

    static String headline(String adjective, String animal) {
        // TODO: Format a reusable story headline.
        throw new UnsupportedOperationException("Format a reusable story headline");
    }

    public static void main(String[] args) {
        var lines = new Scanner(System.in);
        var words = new ArrayList<String>();
        while (words.size() < 4 && lines.hasNextLine()) words.add(lines.nextLine());
        if (words.size() != 4) {
            System.out.println("Enter an animal, a verb, an adjective, and a noun on separate lines.");
            return;
        }
        System.out.println(story(words.get(0), words.get(1), words.get(2), words.get(3)));
        if (args.length == 1 && args[0].equals("headline")) System.out.println(headline(words.get(2), words.get(0)));
    }
}
