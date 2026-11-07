package editor;
import java.util.ArrayList;
import java.util.List;

public final class TextVisitor implements Nodes.Visitor<String> {
    public String text(Nodes.Text node) { return node.value(); }
    public String group(Nodes.Group node) {
        throw new UnsupportedOperationException("Implement recursive text visiting.");
    }
}
