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
        return Optional.ofNullable(notes.get(Note.key(id)));
    }
    public List<Note> all() {
        return List.copyOf(notes.values());
    }
    public void save(Note note) {
        Objects.requireNonNull(note,"note");
        notes.put(note.id(),note);
    }
}
