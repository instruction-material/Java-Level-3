package editor;
import java.util.Objects;
/** A request owns its receiver and validated arguments, and retains opaque snapshots. */
public final class AppendCommand implements EditCommand {
    private final TextBuffer buffer;
    private final String line;
    private TextBuffer.Memento before;
    private TextBuffer.Memento after;
    public AppendCommand(TextBuffer buffer, NoteDate date, String value) {
        this.buffer = Objects.requireNonNull(buffer, "buffer");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(value, "text");
        if (value.isEmpty()) throw new IllegalArgumentException("text");
        line = date.label() + " | " + value + "\n";
    }
    public void execute() {
        if (before != null) throw new IllegalStateException("executed");
        before = buffer.save(); buffer.append(line); after = buffer.save();
    }
    public void undo() { if (before == null) throw new IllegalStateException("unexecuted"); buffer.restore(before); }
    public void redo() { if (after == null) throw new IllegalStateException("unexecuted"); buffer.restore(after); }
}
