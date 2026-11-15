package app;
import api.RenderRule;
public final class PlainRule implements RenderRule {
    public PlainRule() {}
    public String render(String message) { return "text="+message+"\n"; }
}
