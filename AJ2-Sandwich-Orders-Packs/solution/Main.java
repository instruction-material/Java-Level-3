import java.util.*;

class Main {
    static String order(List<String> menu, List<String> answers) {
        if (menu.size() != answers.size()) throw new IllegalArgumentException("One answer is required per ingredient");
        var abbreviation = new StringBuilder();
        for (int i = 0; i < menu.size(); i++) {
            String answer = answers.get(i).strip().toLowerCase(Locale.ROOT);
            String ingredient = menu.get(i).strip();
            if (ingredient.isEmpty()) throw new IllegalArgumentException("Empty ingredient");
            if (answer.equals("yes")) abbreviation.append(ingredient.charAt(0));
            else if (!answer.equals("no")) throw new IllegalArgumentException("Expected yes or no");
        }
        return abbreviation.toString();
    }

    public static void main(String[] args) {
        System.out.println(order(List.of("lettuce", "onion", "cheese"), List.of("yes", "NO", " yes ")));
    }
}
