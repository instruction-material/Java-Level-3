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
        enter();
        try {
            if (!validation.check(text).accepted()) return false;
            String before = document.text();
            EditCommand command = new ReplaceCommand(document, text);
            command.execute();
            history.push(command);
            events.publish(new EditEvent(EditEvent.Kind.REPLACE, before, document.text()));
            return true;
        } finally { busy = false; }
    }
    public boolean undo() {
        enter();
        try {
            if (history.isEmpty()) return false;
            String before = document.text();
            history.peek().undo();
            history.pop();
            events.publish(new EditEvent(EditEvent.Kind.UNDO, before, document.text()));
            return true;
        } finally { busy = false; }
    }
}
