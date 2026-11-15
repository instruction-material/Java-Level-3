package app;
import java.util.List;
public abstract class DocumentTemplate {
    protected DocumentTemplate() {}
    public final String build(List<String> rows) {throw new UnsupportedOperationException("Complete template skeleton");}
    protected abstract String header();
    protected abstract String line(String row);
    protected String footer(){return "";}
}
