package reports;
import java.util.List;
import java.util.Objects;

/** Ordered format algorithm: validate, header, each row, footer. */
public abstract class Report {
    public final String render(List<Row> rows) {
        Objects.requireNonNull(rows, "rows");
        for (Row row : rows) Objects.requireNonNull(row, "row");
        List<Row> snapshot = List.copyOf(rows);
        StringBuilder text = new StringBuilder(header());
        long total = 0;
        for (Row row : snapshot) {
            text.append(rowText(row));
            total += row.count();
        }
        return text.append(footer(total)).toString();
    }
    protected abstract String header();
    protected abstract String rowText(Row row);
    protected abstract String footer(long total);
}
