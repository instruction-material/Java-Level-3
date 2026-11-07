package notes.application;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import notes.domain.Note;
import notes.plugins.PluginCatalog;
import notes.ports.NoteRepository;
import notes.ports.NoteSink;
/** Application layer depends on ports, domain and plugin API, never infrastructure or UI. */
public final class NoteService {
    private final NoteRepository repository;
    private final PluginCatalog plugins;
    private final NoteSink sink;
    private boolean busy;
    public NoteService(NoteRepository repository,PluginCatalog plugins,NoteSink sink) {
        this.repository=Objects.requireNonNull(repository,"repository");
        this.plugins=Objects.requireNonNull(plugins,"plugins");
        this.sink=Objects.requireNonNull(sink,"sink");
    }
    private void enter() { if (busy) throw new IllegalStateException("reentrant mutation"); busy=true; }
    public List<Note> list() { return List.copyOf(repository.all()); }
    public Note create(String id,String text,List<String> pipeline) {
throw new UnsupportedOperationException("Implement note creation.");
    }
    public Note replace(String id,String text,List<String> pipeline) {
throw new UnsupportedOperationException("Implement note replacement.");
    }
    public Note copy(String sourceId,String targetId,List<String> pipeline) {
throw new UnsupportedOperationException("Implement optional note copying.");
    }
}
