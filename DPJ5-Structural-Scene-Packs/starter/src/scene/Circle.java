// Bridge: circle geometry delegates its notation to an injected renderer.
package scene;
import java.util.Objects;

public final class Circle implements Graphic {
    private final Renderer renderer;
    private final String name;
    private final int radius;
    public Circle(Renderer renderer, String name, int radius) {
        this.renderer = Objects.requireNonNull(renderer, "renderer");
        this.name = Graphic.name(name); this.radius = Graphic.size(radius);
    }
    public String render() { throw new UnsupportedOperationException("Implement the circle rendering bridge."); }
}
