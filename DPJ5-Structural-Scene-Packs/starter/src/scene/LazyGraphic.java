// Proxy: load on first render, cache a successful product, and delegate each render.
package scene;
import java.util.Objects;
import java.util.function.Supplier;

public final class LazyGraphic implements Graphic {
    private final Supplier<? extends Graphic> loader;
    private Graphic loaded;
    public LazyGraphic(Supplier<? extends Graphic> loader) { this.loader = Objects.requireNonNull(loader, "loader"); }
    public boolean isLoaded() { return loaded != null; }
    public String render() { throw new UnsupportedOperationException("Implement the lazy product lifecycle."); }
}
