// Template Method: final ordering with explicit, narrowly overridable hooks.
package behavior;
import java.util.List;

public abstract class ReportTemplate {
    protected abstract String heading();
    protected abstract String formatLine(String text);
    protected String footer() { return "End"; }
    public final String render(List<String> input) {
        List<String> lines = List.copyOf(input);
        for (String line : lines) TextRule.line(line);
        StringBuilder result = new StringBuilder(TextRule.line(heading())).append('\n');
        for (String line : lines) result.append(TextRule.line(formatLine(line))).append('\n');
        return result.append(TextRule.line(footer())).append('\n').toString();
    }
}
