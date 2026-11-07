package scene;
import java.util.Objects;

public final class Box implements Graphic {
    private final Renderer renderer;
    private final String name;
    private final int width, height;
    public Box(Renderer renderer, String name, int width, int height) {
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.name = Graphic.name(name); this.width = Graphic.size(width); this.height = Graphic.size(height);
    }
    public String render() { return Objects.requireNonNull(renderer.box(name, width, height), "box text"); }
}
