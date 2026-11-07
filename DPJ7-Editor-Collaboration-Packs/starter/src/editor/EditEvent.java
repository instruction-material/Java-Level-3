package editor;
import java.util.Objects;

public record EditEvent(Kind kind, String before, String after) {
    public enum Kind { REPLACE, UNDO }
    public EditEvent {
        Objects.requireNonNull(kind, "kind");
        before = TextDocument.line(before); after = TextDocument.line(after);
    }
}
