package notes.infra;
import java.util.Locale;
import notes.domain.Note;
import notes.plugins.TextPlugin;
public final class UppercasePlugin implements TextPlugin {
    public UppercasePlugin() {}
    public String id() { return "upper"; }
    public String apply(String text) { return Note.line(text).toUpperCase(Locale.ROOT); }
}
