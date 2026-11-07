package scene;
import java.util.List;
import java.util.Objects;

public final class Formats {
    private Formats() {}
    private static List<String> children(List<String> values) { return List.copyOf(Objects.requireNonNull(values, "children")); }
    public static final class Plain implements Renderer {
        public String circle(String name, int radius) { return "circle(" + Graphic.name(name) + "," + Graphic.size(radius) + ")"; }
        public String box(String name, int width, int height) { return "box(" + Graphic.name(name) + "," + Graphic.size(width) + "x" + Graphic.size(height) + ")"; }
        public String group(List<String> values) { return "group[" + String.join(";", children(values)) + "]"; }
    }
    public static final class Compact implements Renderer {
        public String circle(String name, int radius) { return "C:" + Graphic.name(name) + ":" + Graphic.size(radius); }
        public String box(String name, int width, int height) { return "B:" + Graphic.name(name) + ":" + Graphic.size(width) + ":" + Graphic.size(height); }
        public String group(List<String> values) { return "G(" + String.join("|", children(values)) + ")"; }
    }
}
