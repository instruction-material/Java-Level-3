// Decorator: add one visible frame while preserving the wrapped Graphic contract.
package scene;
import java.util.Objects;

public final class Frame implements Graphic {
    private final Graphic wrapped;
    public Frame(Graphic wrapped) { this.wrapped = Objects.requireNonNull(wrapped, "wrapped"); }
    public Graphic wrapped() { return wrapped; }
    public String render() { throw new UnsupportedOperationException("Implement one-call frame decoration."); }
}
