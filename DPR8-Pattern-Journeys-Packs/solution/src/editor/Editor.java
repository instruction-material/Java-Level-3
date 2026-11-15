package editor;
/** Facade: callers retain four operations while subsystem ownership stays internal. */
public final class Editor implements EditorAPI {
    private final TextBuffer buffer = new TextBuffer();
    private final CommandHistory history = new CommandHistory();
    public void append(int year, int month, int day, String text) {
        NoteDate date = new DayAdapter(new DayExtension(new AncientDay(year, month, day)));
        history.execute(new AppendCommand(buffer, date, text));
    }
    public boolean undo() { return history.undo(); }
    public boolean redo() { return history.redo(); }
    public String text() { return buffer.text(); }
}
