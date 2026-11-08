package notebook.ports;

import java.util.List;
import java.util.Optional;
import notebook.api.Note;

// Sequential calls; save preserves existing position and appends a new ID.
// all returns insertion order; failures propagate before application auditing.
public interface NoteRepository {
    Optional<Note> find(String id);
    List<Note> all();
    void save(Note note);
    void remove(String id);
}
