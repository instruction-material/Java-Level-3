package creation;
import java.util.Objects;

/** A fixed enum choice is a simple static factory, not the Factory Method pattern. */
public final class RendererFactory {
    private RendererFactory() {}
    public static Renderer from(Families.Factory family) {
        Objects.requireNonNull(family, "family");
        return new FamilyRenderer(family.heading(), family.bodyLine());
    }
    public static Renderer forStyle(Style style) {
        Objects.requireNonNull(style, "style");
        return from(switch (style) {
            case PLAIN -> new Families.PlainFactory();
            case BOXED -> new Families.BoxedFactory();
        });
    }
}
