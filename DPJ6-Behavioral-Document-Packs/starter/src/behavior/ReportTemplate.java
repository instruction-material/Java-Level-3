// Template Method: final ordering with explicit, narrowly overridable hooks.
package behavior;
import java.util.List;

public abstract class ReportTemplate {
    protected abstract String heading();
    protected abstract String formatLine(String text);
    protected String footer() { return "End"; }
    public final String render(List<String> input) {
        throw new UnsupportedOperationException("Implement the fixed report skeleton.");
    }
}
