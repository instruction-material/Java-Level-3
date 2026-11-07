package behavior;
import java.util.Locale;

public final class Rules {
    private Rules() {}
    public static final class Identity implements TextRule {
        public String apply(String text) { return TextRule.line(text); }
    }
    public static final class Upper implements TextRule {
        public String apply(String text) { return TextRule.line(text).toUpperCase(Locale.ROOT); }
    }
    public static final class Prefix implements TextRule {
        private final String prefix;
        public Prefix(String prefix) { this.prefix = TextRule.line(prefix); }
        public String apply(String text) { return prefix + TextRule.line(text); }
    }
}
