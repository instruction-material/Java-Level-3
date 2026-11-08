package notebook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import notebook.api.Action;
import notebook.api.AuditSink;
import notebook.api.Note;
import notebook.api.Notebook;

public final class NotebookDesk implements Notebook {
    private final LinkedHashMap<String, Note> notes = new LinkedHashMap<>();
    private final AuditSink audit;
    public NotebookDesk(AuditSink audit) { this.audit = Objects.requireNonNull(audit, "audit"); }
    @Override public Note create(String id, String text) {
        Note note = new Note(id, text, 1);
        if (notes.containsKey(id)) throw new IllegalStateException("duplicate");
        notes.put(id, note);
        audit.record(Action.CREATED, note);
        return note;
    }
    @Override public boolean edit(String id, long expectedVersion, String text) {
        Note.validId(id);
        Note.validText(text);
        Note.validVersion(expectedVersion);
        Note previous = notes.get(id);
        if (previous == null || previous.version() != expectedVersion) return false;
        Note updated = new Note(id, text, Math.addExact(previous.version(), 1));
        notes.put(id, updated);
        audit.record(Action.EDITED, updated);
        return true;
    }
    @Override public Optional<Note> find(String id) {
        Note.validId(id);
        return Optional.ofNullable(notes.get(id));
    }
    @Override public List<Note> all() { return List.copyOf(notes.values()); }
    @Override public boolean remove(String id, long expectedVersion) {
        throw new UnsupportedOperationException("Implement optional versioned removal.");
    }
}
