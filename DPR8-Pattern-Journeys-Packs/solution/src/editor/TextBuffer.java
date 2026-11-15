package editor;
import java.util.Objects;
public final class TextBuffer {
    private String text = "";
    /** Opaque Memento; a caretaker can retain it without reading or rewriting state. */
    public static final class Memento {
        private final TextBuffer owner;
        private final String text;
        private Memento(TextBuffer owner, String text) { this.owner = owner; this.text = text; }
    }
    public String text() { return text; }
    public void append(String line) { text += Objects.requireNonNull(line, "line"); }
    public Memento save() { return new Memento(this, text); }
    public void restore(Memento value) {
        Objects.requireNonNull(value, "memento");
        if (value.owner != this) throw new IllegalArgumentException("owner");
        text = value.text;
    }
}
