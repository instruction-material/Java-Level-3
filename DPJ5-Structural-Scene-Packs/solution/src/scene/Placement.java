package scene;
import java.util.Objects;

public record Placement(GlyphPool.Glyph glyph, int x, int y) {
    public Placement {
        Objects.requireNonNull(glyph, "glyph");
        if (x < 0 || x > 1000 || y < 0 || y > 1000) throw new IllegalArgumentException("position must be 0..1000");
    }
    public String render() { return glyph.symbol() + ":" + glyph.tone() + "@" + x + "," + y; }
    public Placement shift(int dx, int dy) { return new Placement(glyph, Math.addExact(x, dx), Math.addExact(y, dy)); }
}
