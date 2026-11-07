package notes.infra;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import notes.domain.Note;
import notes.ports.NoteRepository;
public final class InMemoryNotes implements NoteRepository {
    private final Map<String,Note> notes=new LinkedHashMap<>();
    public Optional<Note> find(String id) {
        throw new UnsupportedOperationException("Implement repository lookup.");
    }
    public List<Note> all() {
        throw new UnsupportedOperationException("Implement an immutable repository view.");
    }
    public void save(Note note) {
        throw new UnsupportedOperationException("Implement repository save.");
    }
}
