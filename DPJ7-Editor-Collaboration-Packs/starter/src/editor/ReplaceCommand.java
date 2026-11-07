// Command owns one execution and its memento, with a value-based stale-undo guard.
package editor;
import java.util.Objects;

public final class ReplaceCommand implements EditCommand {
    private enum Phase { NEW, DONE, UNDONE }
    private final TextDocument document;
    private final String replacement;
    private TextDocument.Snapshot before;
    private Phase phase = Phase.NEW;
    public ReplaceCommand(TextDocument document, String replacement) {
        this.document = Objects.requireNonNull(document, "document");
        this.replacement = TextDocument.line(replacement);
    }
    public String phase() { return phase.name(); }
    public void execute() {
        throw new UnsupportedOperationException("Implement one-shot command execution.");
    }
    public void undo() {
        throw new UnsupportedOperationException("Implement guarded command undo.");
    }
}
