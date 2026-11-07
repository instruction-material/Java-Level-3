// Abstract Factory: create a compatible heading and body-line family.
package creation;
import java.util.Objects;

public final class Families {
    private Families() {}
    public interface Factory {
        Heading heading();
        BodyLine bodyLine();
    }
    public static final class PlainHeading implements Heading {
        public Style style() { return Style.PLAIN; }
        public String render(String title) { return Objects.requireNonNull(title, "title") + "\n"; }
    }
    public static final class PlainLine implements BodyLine {
        public Style style() { return Style.PLAIN; }
        public String render(String text) { return Objects.requireNonNull(text, "text") + "\n"; }
    }
    public static final class BoxedHeading implements Heading {
        public Style style() { return Style.BOXED; }
        public String render(String title) { return "[" + Objects.requireNonNull(title, "title") + "]\n"; }
    }
    public static final class BoxedLine implements BodyLine {
        public Style style() { return Style.BOXED; }
        public String render(String text) { return "| " + Objects.requireNonNull(text, "text") + " |\n"; }
    }
    public static final class PlainFactory implements Factory {
        public Heading heading() { return new PlainHeading(); }
        public BodyLine bodyLine() { return new PlainLine(); }
    }
    public static final class BoxedFactory implements Factory {
        public Heading heading() { throw new UnsupportedOperationException("Implement the boxed heading product."); }
        public BodyLine bodyLine() { throw new UnsupportedOperationException("Implement the boxed body-line product."); }
    }
}
