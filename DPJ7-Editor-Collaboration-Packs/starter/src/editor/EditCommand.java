package editor;

public interface EditCommand {
    void execute();
    void undo();
}
