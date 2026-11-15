package extension;
import api.RenderRule;
public final class PrefixRule implements RenderRule {
    public PrefixRule(String prefix,RenderRule delegate) {
        throw new UnsupportedOperationException("Complete prefix rule");
    }
    public String render(String message) { throw new UnsupportedOperationException("Complete prefix rule"); }
}
