package scene;
import java.util.List;

/** Rendering notation varies independently of a graphic's geometry. */
public interface Renderer {
    String circle(String name, int radius);
    String box(String name, int width, int height);
    String group(List<String> renderedChildren);
}
