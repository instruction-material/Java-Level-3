import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class Main {
    @FunctionalInterface public interface Node { String render(); }
    @FunctionalInterface public interface TextStyle { String apply(String text); }
    public static final class Plain implements TextStyle {
        public String apply(String text) { return Objects.requireNonNull(text, "text"); }
    }
    public static final class Upper implements TextStyle {
        public String apply(String text) { return Objects.requireNonNull(text, "text").toUpperCase(Locale.ROOT); }
    }
    public abstract static class Message implements Node {
        private final TextStyle style;
        protected Message(TextStyle style) { this.style = Objects.requireNonNull(style, "style"); }
        protected final String styled(String text) { return Objects.requireNonNull(style.apply(text), "styled text"); }
    }
    public static final class Greeting extends Message {
        private final String text;
        public Greeting(TextStyle style, String text) { super(style); this.text = Objects.requireNonNull(text, "text"); }
        public String render() { return styled(text); }
    }
    public static final class Warning extends Message {
        private final String text;
        public Warning(TextStyle style, String text) { super(style); this.text = Objects.requireNonNull(text, "text"); }
        public String render() { return styled("Warning: " + text); }
    }
    public record Sequence(List<Node> children) implements Node {
        public Sequence { children = List.copyOf(children); }
        public String render() {
            List<String> values = new ArrayList<>();
            for (Node child : children) values.add(Objects.requireNonNull(child.render(), "child text"));
            return "[" + String.join(",", values) + "]";
        }
    }
    public record Emphasis(Node child) implements Node {
        public Emphasis { Objects.requireNonNull(child, "child"); }
        public String render() { return "*" + Objects.requireNonNull(child.render(), "child text") + "*"; }
    }
    public static final class LazyNode implements Node {
        private final Supplier<? extends Node> loader;
        private Node cached;
        public LazyNode(Supplier<? extends Node> loader) { this.loader = Objects.requireNonNull(loader, "loader"); }
        public boolean loaded() { return cached != null; }
        public String render() {
            if (cached == null) cached = Objects.requireNonNull(loader.get(), "node");
            return Objects.requireNonNull(cached.render(), "node text");
        }
    }
    public enum Lexeme { READY, WAIT }
    public record Word(String text) {
        public Word { Objects.requireNonNull(text, "text"); }
    }
    public static final class WordPool {
        private final Map<Lexeme, Word> words = new EnumMap<>(Lexeme.class);
        public Word get(Lexeme key) {
            Objects.requireNonNull(key, "key");
            return words.computeIfAbsent(key, value -> new Word(value.name().toLowerCase(Locale.ROOT)));
        }
    }
    public record At(Word word, int row) implements Node {
        public At {
            Objects.requireNonNull(word, "word");
            if (row < 0 || row > 9) throw new IllegalArgumentException("row must be 0..9");
        }
        public String render() { return word.text() + "@" + row; }
    }
    public static void main(String[] args) {
        Node messages = new Sequence(List.of(new Greeting(new Plain(), "Ready"),
            new Emphasis(new Warning(new Upper(), "wait")),
            new LazyNode(() -> new Greeting(new Upper(), "go"))));
        System.out.println(messages.render());
        WordPool pool = new WordPool();
        At first = new At(pool.get(Lexeme.READY), 1), second = new At(pool.get(Lexeme.READY), 2);
        System.out.println(first.render()); System.out.println(second.render());
        System.out.println(first.word() == second.word());
    }
}
