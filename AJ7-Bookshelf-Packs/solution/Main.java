import java.util.ArrayList;
import java.util.List;

public class Main {
    record Book(String title, int pages) {}

    static List<Book> arrange(List<Book> books) {
        var remaining = new ArrayList<>(books);
        List<Book> result = new ArrayList<>(books.size());
        while (!remaining.isEmpty()) {
            int largest = 0;
            for (int candidate = 1; candidate < remaining.size(); candidate++) {
                if (remaining.get(candidate).pages() > remaining.get(largest).pages()) {
                    largest = candidate;
                }
            }
            result.add(remaining.remove(largest));
        }
        return result;
    }

    public static void main(String[] args) {
        List<Book> books = List.of(new Book("Reef", 180), new Book("Drift", 250),
                new Book("Current", 180), new Book("Jetty", 90));
        System.out.println(arrange(books).stream().map(Book::title).toList());
    }
}
