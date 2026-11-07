// Visitor: the stable node family dispatches to independently defined operations.
package editor;
import java.util.List;
import java.util.Objects;

public final class Nodes {
    private Nodes() {}
    public sealed interface Node permits Text, Group {
        <R> R accept(Visitor<R> visitor);
    }
    public interface Visitor<R> {
        R text(Text node);
        R group(Group node);
    }
    public record Text(String value) implements Node {
        public Text { value = TextDocument.line(value); }
        public <R> R accept(Visitor<R> visitor) {
            return Objects.requireNonNull(Objects.requireNonNull(visitor, "visitor").text(this), "visitor result");
        }
    }
    public record Group(List<Node> children) implements Node {
        public Group { children = List.copyOf(children); }
        public <R> R accept(Visitor<R> visitor) {
            return Objects.requireNonNull(Objects.requireNonNull(visitor, "visitor").group(this), "visitor result");
        }
    }
    public static final class LeafCount implements Visitor<Integer> {
        public Integer text(Text node) { return 1; }
        public Integer group(Group node) {
            int total = 0;
            for (Node child : node.children()) total = Math.addExact(total, child.accept(this));
            return total;
        }
    }
}
