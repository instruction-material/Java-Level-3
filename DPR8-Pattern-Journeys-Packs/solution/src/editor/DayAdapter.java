package editor;
import java.util.Objects;
/** Adapter: exposes the client-required NoteDate contract. */
public final class DayAdapter implements NoteDate {
    private final DayExtension date;
    public DayAdapter(DayExtension date) { this.date = Objects.requireNonNull(date, "date"); }
    public String label() { return date.iso(); }
}
