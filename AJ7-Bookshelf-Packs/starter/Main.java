import java.util.ArrayList;
import java.util.List;

public class Main {
    record Book(String title, int pages) {}

    static List<Book> arrange(List<Book> books) {
        // TODO: Select the largest remaining book from a copy.
        throw new UnsupportedOperationException("Select the largest remaining book from a copy");
    }

    public static void main(String[] args) {
        List<Book> books = List.of(new Book("Reef", 180), new Book("Drift", 250),
                new Book("Current", 180), new Book("Jetty", 90));
        System.out.println(arrange(books).stream().map(Book::title).toList());
    }
}
