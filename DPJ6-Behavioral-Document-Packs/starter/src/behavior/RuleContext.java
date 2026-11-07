// Strategy: the caller chooses the algorithm; the context delegates one call.
package behavior;
import java.util.Objects;

public final class RuleContext {
    private final TextRule rule;
    public RuleContext(TextRule rule) { this.rule = Objects.requireNonNull(rule, "rule"); }
    public String format(String text) { throw new UnsupportedOperationException("Implement the selected text strategy."); }
}
