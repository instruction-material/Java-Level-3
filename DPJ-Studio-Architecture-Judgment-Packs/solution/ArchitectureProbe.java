import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import notebook.api.Note;
import notebook.application.NotebookService;
import notebook.ports.NoteRepository;

public final class ArchitectureProbe {
    private ArchitectureProbe() {}
    private static void require(boolean value) { if (!value) throw new AssertionError(); }
    private static final class FakeNotes implements NoteRepository {
        private final List<Note> values = new ArrayList<>();
        private RuntimeException saveFailure;
        @Override public Optional<Note> find(String id) {
            return values.stream().filter(n -> n.id().equals(id)).findFirst();
        }
        @Override public List<Note> all() { return values; }
        @Override public void save(Note note) {
            if (saveFailure != null) throw saveFailure;
            int index = -1;
            for (int i = 0; i < values.size(); i++) if (values.get(i).id().equals(note.id())) index = i;
            if (index < 0) values.add(note); else values.set(index, note);
        }
        @Override public void remove(String id) { values.removeIf(n -> n.id().equals(id)); }
    }
    public static void main(String[] args) {
        FakeNotes repository = new FakeNotes();
        List<Note> audit = new ArrayList<>();
        NotebookService book = new NotebookService(repository, (a, n) -> {
            require(repository.find(n.id()).orElseThrow().equals(n));
            audit.add(n);
        });
        book.create("plan", "Read");
        List<Note> before = book.all();
        book.edit("plan", 1, "Sketch");
        require(before.equals(List.of(new Note("plan", "Read", 1))));
        require(audit.size() == 2 && repository.values.getFirst().version() == 2);
        RuntimeException marker = new IllegalStateException("storage");
        repository.saveFailure = marker;
        try { book.edit("plan", 2, "Failed"); throw new AssertionError(); }
        catch (RuntimeException failure) { require(failure == marker); }
        require(book.find("plan").orElseThrow().version() == 2 && audit.size() == 2);
        repository.saveFailure = null;
        repository.values.clear();
        repository.values.add(new Note("limit", "X", Long.MAX_VALUE));
        try { book.edit("limit", Long.MAX_VALUE, "Overflow"); throw new AssertionError(); }
        catch (ArithmeticException expected) { require(book.find("limit").orElseThrow().version() == Long.MAX_VALUE); }
        require(audit.size() == 2);
        System.out.println("Isolated application seam and failure contracts passed");
    }
}
