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
        if (phase != Phase.NEW) throw new IllegalStateException("command already used");
        before = document.snapshot();
        document.replace(replacement);
        phase = Phase.DONE;
    }
    public void undo() {
        if (phase != Phase.DONE) throw new IllegalStateException("command is not done");
        if (!document.text().equals(replacement)) throw new IllegalStateException("stale undo");
        document.restore(before);
        phase = Phase.UNDONE;
    }
}
