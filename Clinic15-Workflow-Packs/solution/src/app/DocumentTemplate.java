package app;
import api.Rows;
import java.util.*;
public abstract class DocumentTemplate {
    protected DocumentTemplate() {}
    public final String build(List<String> rows) {
        List<String> input=Rows.copy(rows);
        StringBuilder text=new StringBuilder(Objects.requireNonNull(header(),"header"));
        for(String row:input)text.append(Objects.requireNonNull(line(row),"line"));
        text.append(Objects.requireNonNull(footer(),"footer"));
        return text.toString();
    }
    protected abstract String header();
    protected abstract String line(String row);
    protected String footer(){return "";}
}
