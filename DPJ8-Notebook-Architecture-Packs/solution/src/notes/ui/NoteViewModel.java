package notes.ui;
import java.util.List;
import java.util.Objects;
import notes.application.NoteService;
import notes.domain.Note;
/** Explicit refresh is the presentation seam; no toolkit or implicit binding is claimed. */
public final class NoteViewModel {
    public record State(List<String> rows) { public State { rows=List.copyOf(rows); } }
    private final NoteService service;
    private State state=new State(List.of());
    public NoteViewModel(NoteService service) { this.service=Objects.requireNonNull(service,"service"); }
    public State state() { return state; }
    public void refresh() {
        List<String> rows=service.list().stream().map(note -> note.id()+"="+note.text()).toList();
        state=new State(rows);
    }
    public Note add(String id,String text,List<String> pipeline) {
        Note created=service.create(id,text,pipeline);
        refresh();
        return created;
    }
}
