package editor;
import java.util.ArrayList;
import java.util.List;

public final class TextVisitor implements Nodes.Visitor<String> {
    public String text(Nodes.Text node) { return node.value(); }
    public String group(Nodes.Group node) {
        List<String> parts = new ArrayList<>();
        for (Nodes.Node child : node.children()) parts.add(child.accept(this));
        return "[" + String.join(",", parts) + "]";
    }
}
