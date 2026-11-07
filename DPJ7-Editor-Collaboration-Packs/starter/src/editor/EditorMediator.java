// Mediator coordinates peers; model/history commit before observer notification.
package editor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public final class EditorMediator {
    private final TextDocument document;
    private final Validation validation;
    private final EventBus events;
    private final Deque<EditCommand> history = new ArrayDeque<>();
    private boolean busy;
    public EditorMediator(String initial, Validation validation, EventBus events) {
        document = new TextDocument(initial);
        this.validation = Objects.requireNonNull(validation, "validation");
        this.events = Objects.requireNonNull(events, "events");
    }
    public String text() { return document.text(); }
    public int undoDepth() { return history.size(); }
    private void enter() { if (busy) throw new IllegalStateException("reentrant editor operation"); busy = true; }
    public boolean replace(String text) {
        throw new UnsupportedOperationException("Implement coordinated replacement.");
    }
    public boolean undo() {
        throw new UnsupportedOperationException("Implement coordinated undo.");
    }
}
