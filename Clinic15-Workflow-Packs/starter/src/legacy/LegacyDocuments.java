package legacy;
import api.*;
import java.util.*;
public final class LegacyDocuments implements Documents {
    public LegacyDocuments() {}
    public String build(String kind,List<String> rows) {
        Objects.requireNonNull(kind,"kind");List<String> input=Rows.copy(rows);
        if(kind.equals("plain")) {
            StringBuilder text=new StringBuilder("begin\n");
            for(String row:input)text.append(row).append('\n');
            text.append("end\n");return text.toString();
        }
        if(kind.equals("marked")) {
            StringBuilder text=new StringBuilder("BEGIN\n");
            for(String row:input)text.append("* ").append(row).append('\n');
            text.append("END\n");return text.toString();
        }
        throw new IllegalArgumentException("kind");
    }
}
