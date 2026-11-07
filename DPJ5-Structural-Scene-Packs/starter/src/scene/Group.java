// Composite: an immutable child-list snapshot preserves child order and identity.
package scene;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Group implements Graphic {
    private final Renderer renderer;
    private final List<Graphic> children;
    public Group(Renderer renderer, List<Graphic> children) {
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.children = List.copyOf(Objects.requireNonNull(children, "children"));
    }
    public List<Graphic> children() { return children; }
    public String render() { throw new UnsupportedOperationException("Implement ordered composite rendering."); }
}
