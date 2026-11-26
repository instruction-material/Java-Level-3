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
        return List.copyOf(shelf(title).waiting());
    }

    public String apply(Event event) {
        Shelf shelf = shelf(event.title());
        String member = event.member();
        String title = event.title();
        if (member.isBlank()) throw new IllegalArgumentException("Expected a member ID");
        return switch (event) {
            case Event.Request ignored -> {
                if (shelf.borrowers().contains(member) || shelf.waiting().contains(member)) {
                    yield "duplicate " + member + " " + title;
                }
                if (shelf.free() > 0) {
                    shelf.borrowers().add(member);
                    yield "loan " + member + " " + title;
                }
                shelf.waiting().add(member);
                yield "waiting " + member + " " + title;
            }
            case Event.Return ignored -> {
                if (!shelf.borrowers().remove(member)) yield "no loan " + member + " " + title;
                if (shelf.waiting().isEmpty()) yield "returned " + member + " " + title;
                String next = shelf.waiting().removeFirst();
                shelf.borrowers().add(next);
                yield "handoff " + member + " " + next + " " + title;
            }
            case Event.Cancel ignored -> shelf.waiting().remove(member)
                ? "cancelled " + member + " " + title : "no wait " + member + " " + title;
        };
    }
}
