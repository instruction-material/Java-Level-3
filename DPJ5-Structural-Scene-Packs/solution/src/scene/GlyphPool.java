// Flyweight: share immutable intrinsic glyph state within one bounded pool.
package scene;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class GlyphPool {
    public enum Tone { INK, ACCENT }
    public record Glyph(char symbol, Tone tone) {
        public Glyph {
            Objects.requireNonNull(tone, "tone");
            if (symbol < 'A' || symbol > 'Z') throw new IllegalArgumentException("symbol must be A..Z");
        }
    }
    private record Key(char symbol, Tone tone) {}
    private final Map<Key, Glyph> pool = new HashMap<>();
    public int size() { return pool.size(); }
    public Glyph get(char symbol, Tone tone) {
        Objects.requireNonNull(tone, "tone");
        if (symbol < 'A' || symbol > 'Z') throw new IllegalArgumentException("symbol must be A..Z");
        return pool.computeIfAbsent(new Key(symbol, tone), key -> new Glyph(key.symbol(), key.tone()));
    }
}
