package notes.ports;
import java.util.List;
import java.util.Optional;
import notes.domain.Note;
/** Single-threaded upsert store; all returns an immutable insertion-order snapshot. */
public interface NoteRepository {
    Optional<Note> find(String id);
    List<Note> all();
    void save(Note note);
}
