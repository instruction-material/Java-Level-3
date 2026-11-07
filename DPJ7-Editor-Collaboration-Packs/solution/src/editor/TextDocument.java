package editor;
import java.util.Objects;

/** Owns validated text and opaque snapshots tied to this document. */
public final class TextDocument {
    private String text;
    public TextDocument(String text) { this.text = line(text); }
    public static String line(String text) {
        Objects.requireNonNull(text, "text");
        if (text.indexOf('\n') >= 0 || text.indexOf('\r') >= 0)
            throw new IllegalArgumentException("text must exclude CR/LF");
        return text;
    }
    public String text() { return text; }
    public void replace(String text) { this.text = line(text); }
    public static final class Snapshot {
        private final TextDocument owner;
        private final String text;
        private Snapshot(TextDocument owner, String text) { this.owner = owner; this.text = text; }
    }
    public Snapshot snapshot() { return new Snapshot(this, text); }
    public void restore(Snapshot snapshot) {
        Objects.requireNonNull(snapshot, "snapshot");
        if (snapshot.owner != this) throw new IllegalArgumentException("foreign snapshot");
        text = snapshot.text;
    }
}
