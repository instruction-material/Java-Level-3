package notes.infra;
import notes.domain.Note;
import notes.plugins.TextPlugin;
public final class BracketPlugin implements TextPlugin {
    public BracketPlugin() {}
    public String id() { return "bracket"; }
    public String apply(String text) { return "["+Note.line(text)+"]"; }
}
