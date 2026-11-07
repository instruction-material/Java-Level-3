import java.util.Objects;
import java.util.function.Supplier;

/** Small, complete comparisons; no assignment implementation is hidden here. */
public class Main {
    public record Label(String text) {
        public Label { Objects.requireNonNull(text, "text"); }
        public static Label named(String text) { return new Label(text); }
    }
    /** A supplier may return a cached value. This method calls it exactly once. */
    public static Label supplied(Supplier<Label> supplier) {
        return Objects.requireNonNull(Objects.requireNonNull(supplier, "supplier").get(), "label");
    }
    public interface Token { String render(); }
    public record PlainToken(String text) implements Token {
        public PlainToken { Objects.requireNonNull(text, "text"); }
        public String render() { return text; }
    }
    public record BracketToken(String text) implements Token {
        public BracketToken { Objects.requireNonNull(text, "text"); }
        public String render() { return "[" + text + "]"; }
    }
    public abstract static class Creator {
        protected abstract Token create(String text);
        public final String display(String text) {
            Objects.requireNonNull(text, "text");
            Token token = Objects.requireNonNull(create(text), "token");
            return Objects.requireNonNull(token.render(), "rendered text");
        }
    }
    public static final class Brackets extends Creator {
        protected Token create(String text) { return new BracketToken(text); }
    }
    public interface Heading { String render(String text); }
    public interface Body { String render(String text); }
    /** Two product types vary together through one injected family. */
    public interface Theme { Heading heading(); Body body(); }
    public static final class PlainTheme implements Theme {
        public Heading heading() { return text -> Objects.requireNonNull(text, "text"); }
        public Body body() { return text -> Objects.requireNonNull(text, "text"); }
    }
    public static final class MarkedTheme implements Theme {
        public Heading heading() { return text -> "**" + Objects.requireNonNull(text, "text") + "**"; }
        public Body body() { return text -> "> " + Objects.requireNonNull(text, "text"); }
    }
    public static String themed(Theme theme, String heading, String body) {
        Objects.requireNonNull(theme, "theme");
        Objects.requireNonNull(heading, "heading");
        Objects.requireNonNull(body, "body");
        return theme.heading().render(heading) + "\n" + theme.body().render(body);
    }
    /** The constructor is sufficient for a small fixed configuration. */
    public record Page(String title, String body, boolean emphasize) {
        public Page {
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(body, "body");
        }
        public String render() { return (emphasize ? "[" + title + "]" : title) + "\n" + body; }
    }
    /** A fluent configuration builder, a common Java variation of Builder. */
    public static final class PageBuilder {
        private final String title;
        private String body = "";
        private boolean emphasize;
        public PageBuilder(String title) { this.title = Objects.requireNonNull(title, "title"); }
        public PageBuilder body(String value) { body = Objects.requireNonNull(value, "body"); return this; }
        public PageBuilder emphasize(boolean value) { emphasize = value; return this; }
        public Page build() { return new Page(title, body, emphasize); }
    }
    public static void main(String[] args) {
        System.out.println(new Label("Ready").text());
        System.out.println(Label.named("Ready").text());
        System.out.println(supplied(() -> new Label("Ready")).text());
        System.out.println(new Brackets().display("Ready"));
        System.out.println(themed(new MarkedTheme(), "Title", "Body"));
        System.out.println(new PageBuilder("Title").body("Body").emphasize(true).build().render());
    }
}
