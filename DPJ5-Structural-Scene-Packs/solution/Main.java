import java.util.List;
import scene.*;

public class Main {
    public static void main(String[] args) {
        Renderer plain = new Formats.Plain();
        Graphic scene = new Group(plain, List.of(new Circle(plain, "moon", 2),
            new Frame(new Box(plain, "tile", 3, 4)),
            new LazyGraphic(() -> new Circle(new Formats.Compact(), "sun", 1))));
        System.out.println(scene.render());
        GlyphPool pool = new GlyphPool();
        Placement a = new Placement(pool.get('A', GlyphPool.Tone.INK), 1, 2);
        Placement b = new Placement(pool.get('A', GlyphPool.Tone.INK), 5, 6);
        System.out.println(a.render()); System.out.println(b.render());
        System.out.println(a.glyph() == b.glyph());
    }
}
