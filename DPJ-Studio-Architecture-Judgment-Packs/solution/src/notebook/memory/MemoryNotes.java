package notebook.memory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import notebook.api.Note;
import notebook.ports.NoteRepository;

public final class MemoryNotes implements NoteRepository {
    private final LinkedHashMap<String, Note> notes = new LinkedHashMap<>();
    @Override public Optional<Note> find(String id) { return Optional.ofNullable(notes.get(id)); }
    @Override public List<Note> all() { return List.copyOf(notes.values()); }
    @Override public void save(Note note) { notes.put(note.id(), note); }
    @Override public void remove(String id) { notes.remove(id); }
}
