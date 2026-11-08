package notebook.application;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import notebook.api.Action;
import notebook.api.AuditSink;
import notebook.api.Note;
import notebook.api.Notebook;
import notebook.ports.NoteRepository;

public final class NotebookService implements Notebook {
    private final NoteRepository repository;
    private final AuditSink audit;
    public NotebookService(NoteRepository repository, AuditSink audit) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.audit = Objects.requireNonNull(audit, "audit");
    }
    @Override public Note create(String id, String text) {
        Note note = new Note(id, text, 1);
        if (repository.find(id).isPresent()) throw new IllegalStateException("duplicate");
        repository.save(note);
        audit.record(Action.CREATED, note);
        return note;
    }
    @Override public boolean edit(String id, long expectedVersion, String text) {
        Note.validId(id);
        Note.validText(text);
        Note.validVersion(expectedVersion);
        Note previous = repository.find(id).orElse(null);
        if (previous == null || previous.version() != expectedVersion) return false;
        Note updated = new Note(id, text, Math.addExact(previous.version(), 1));
        repository.save(updated);
        audit.record(Action.EDITED, updated);
        return true;
    }
    @Override public Optional<Note> find(String id) {
        Note.validId(id);
        return repository.find(id);
    }
    @Override public List<Note> all() { return List.copyOf(repository.all()); }
    @Override public boolean remove(String id, long expectedVersion) {
        Note.validId(id);
        Note.validVersion(expectedVersion);
        Note previous = repository.find(id).orElse(null);
        if (previous == null || previous.version() != expectedVersion) return false;
        repository.remove(id);
        audit.record(Action.REMOVED, previous);
        return true;
    }
}
