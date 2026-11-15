package editor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
public final class CommandHistory {
    private final Deque<EditCommand> done = new ArrayDeque<>();
    private final Deque<EditCommand> undone = new ArrayDeque<>();
    public void execute(EditCommand command) {
        Objects.requireNonNull(command, "command").execute();
        done.push(command); undone.clear();
    }
    public boolean undo() {
        if (done.isEmpty()) return false;
        EditCommand command = done.peek(); command.undo(); done.pop(); undone.push(command); return true;
    }
    public boolean redo() {
        if (undone.isEmpty()) return false;
        EditCommand command = undone.peek(); command.redo(); undone.pop(); done.push(command); return true;
    }
}
