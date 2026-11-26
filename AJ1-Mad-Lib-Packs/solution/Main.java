import java.util.*;

class Main {
    static String story(String animal, String verb, String adjective, String noun) {
        return "A " + adjective.strip() + " " + animal.strip() + " likes to " + verb.strip() + " with " + noun.strip() + ".";
    }

    static String headline(String adjective, String animal) {
        return (adjective.strip() + " " + animal.strip()).toUpperCase(Locale.ROOT) + " ADVENTURE";
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
