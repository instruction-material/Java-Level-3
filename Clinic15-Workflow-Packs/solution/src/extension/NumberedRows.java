package extension;
import api.Rows;
import java.util.List;
public final class NumberedRows {
    private NumberedRows() {}
    public static String render(List<String> rows,boolean footer) {
        List<String> input=Rows.copy(rows);StringBuilder text=new StringBuilder("ROWS\n");
        for(int n=0;n<input.size();n++)text.append(n+1).append(':').append(input.get(n)).append('\n');
        if(footer)text.append("total=").append(input.size()).append('\n');
        return text.toString();
    }
}
