package app;
import api.RenderRule;
public final class CompactRule implements RenderRule {
    public CompactRule() {}
    public String render(String message) { return message.length()+"|"+message+"\n"; }
}
