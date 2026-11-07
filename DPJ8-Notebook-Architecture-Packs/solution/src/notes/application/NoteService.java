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
        enter();
        try {
            Note.key(id);
            if (repository.find(id).isPresent()) throw new IllegalArgumentException("duplicate note");
            Note note=new Note(id,plugins.apply(text,pipeline));
            repository.save(note);
            sink.saved(note);
            return note;
        } finally { busy=false; }
    }
    public Note replace(String id,String text,List<String> pipeline) {
        enter();
        try {
            Note.key(id);
            if (repository.find(id).isEmpty()) throw new NoSuchElementException("missing note");
            Note note=new Note(id,plugins.apply(text,pipeline));
            repository.save(note);
            sink.saved(note);
            return note;
        } finally { busy=false; }
    }
    public Note copy(String sourceId,String targetId,List<String> pipeline) {
        enter();
        try {
            Note.key(sourceId); Note.key(targetId);
            Note source=repository.find(sourceId).orElseThrow(() -> new NoSuchElementException("missing source"));
            if (repository.find(targetId).isPresent()) throw new IllegalArgumentException("duplicate target");
            Note note=new Note(targetId,plugins.apply(source.text(),pipeline));
            repository.save(note);
            sink.saved(note);
            return note;
        } finally { busy=false; }
    }
}
