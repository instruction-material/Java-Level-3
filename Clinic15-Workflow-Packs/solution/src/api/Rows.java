package api;
import java.util.*;
public final class Rows {
    private Rows() {}
    public static List<String> copy(List<String> rows) {
        Objects.requireNonNull(rows,"rows");
        if(rows.size()>6)throw new IllegalArgumentException("rows");
        List<String> input=new ArrayList<>(rows);
        for(String row:input) {
            Objects.requireNonNull(row,"row");
            if(row.length()>16||row.indexOf('\r')>=0||row.indexOf('\n')>=0||row.indexOf('|')>=0)
                throw new IllegalArgumentException("row");
        }
        return List.copyOf(input);
    }
}
