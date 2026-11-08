package notebook.api;

import java.util.Objects;

public record Note(String id, String text, long version) {
    public Note {
        validId(id);
        validText(text);
        if (version < 1) throw new IllegalArgumentException("version");
    }
    public static void validId(String id) {
        Objects.requireNonNull(id, "id");
        if (!id.matches("[a-z][a-z0-9-]{0,15}")) throw new IllegalArgumentException("id");
    }
    public static void validText(String text) {
        Objects.requireNonNull(text, "text");
        if (text.codePointCount(0, text.length()) > 80 || text.contains("\n") || text.contains("\r")) {
            throw new IllegalArgumentException("text");
        }
    }
    public static void validVersion(long version) {
        if (version < 1) throw new IllegalArgumentException("expectedVersion");
    }
}
