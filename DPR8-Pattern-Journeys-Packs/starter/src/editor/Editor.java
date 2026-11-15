package editor;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
/** Working candidate: extract date boundary, command history and opaque snapshots. */
public final class Editor implements EditorAPI {
    private String text = "";
    private final Deque<String> done = new ArrayDeque<>();
    private final Deque<String> undone = new ArrayDeque<>();
    public void append(int year, int month, int day, String value) {
        AncientDay date = new AncientDay(year, month, day);
        Objects.requireNonNull(value, "text");
        if (value.isEmpty()) throw new IllegalArgumentException("text");
        String stamp = date.y() + "-" + (date.m() < 10 ? "0" : "") + date.m() + "-" + (date.d() < 10 ? "0" : "") + date.d();
        String next = text + stamp + " | " + value + "\n";
        done.push(text); text = next; undone.clear();
    }
    public boolean undo() { if (done.isEmpty()) return false; undone.push(text); text = done.pop(); return true; }
    public boolean redo() { if (undone.isEmpty()) return false; done.push(text); text = undone.pop(); return true; }
    public String text() { return text; }
}
