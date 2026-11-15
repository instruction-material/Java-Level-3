package extension;
import api.*;
import java.util.Objects;
public final class PrefixRule implements RenderRule {
    private final String prefix;
    private final RenderRule delegate;
    public PrefixRule(String prefix,RenderRule delegate) {
        Objects.requireNonNull(prefix,"prefix");
        if(prefix.length()>12||prefix.indexOf('\r')>=0||prefix.indexOf('\n')>=0)
            throw new IllegalArgumentException("prefix");
        this.prefix=prefix;this.delegate=Objects.requireNonNull(delegate,"delegate");
    }
    public String render(String message) {
        Rules.text(message);
        return prefix+Objects.requireNonNull(delegate.render(message),"output");
    }
}
