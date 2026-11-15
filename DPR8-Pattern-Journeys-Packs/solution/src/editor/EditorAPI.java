package editor;
public interface EditorAPI {
    void append(int year, int month, int day, String text);
    boolean undo();
    boolean redo();
    String text();
}
