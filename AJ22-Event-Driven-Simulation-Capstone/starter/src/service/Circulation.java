package service;
import model.Event;
import java.util.*;

public final class Circulation {
    private record Shelf(int copies, Set<String> borrowers, List<String> waiting) {
        int free() { return copies - borrowers.size(); }
    }

    private final Map<String, Shelf> shelves = new HashMap<>();

    public Circulation(Map<String,Integer> inventory) {
        inventory.forEach((title, copies) -> {
            if (title.isBlank() || copies < 1) throw new IllegalArgumentException("Invalid inventory entry");
            shelves.put(title, new Shelf(copies, new HashSet<>(), new ArrayList<>()));
        });
    }

    private Shelf shelf(String title) {
        Shelf shelf = shelves.get(title);
        if (shelf == null) throw new IllegalArgumentException("Unknown title " + title);
        return shelf;
    }

    public int available(String title) { return shelf(title).free(); }

    public List<String> waiting(String title) {
        // TODO in the optional review: prevent this method from exposing the internal list.
        return shelf(title).waiting();
    }

    public String apply(Event event) {
        Shelf shelf = shelf(event.title());
        String member = event.member();
        String title = event.title();
        if (member.isBlank()) throw new IllegalArgumentException("Expected a member ID");
        throw new UnsupportedOperationException("Apply the next circulation event with FIFO handoff");
    }
}
