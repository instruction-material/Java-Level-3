package reports;
import java.util.List;
import java.util.Objects;

/** Working candidate: extract format subclasses, a template, and CSV-only members. */
public final class ReportService {
    private ReportService() {}
    private abstract static class BaseOutput {
        protected final char delimiter = ',';
        protected String quoteLabel(String label) {
            return "\"" + label.replace("\"", "\"\"") + "\"";
        }
    }
    private static final class GeneralOutput extends BaseOutput {
        String render(Format format, List<Row> rows) {
            if (format == Format.PLAIN) {
                StringBuilder text = new StringBuilder("Inventory\n");
                long total = 0;
                for (Row row : rows) {
                    text.append(row.label()).append('=').append(row.count()).append('\n');
                    total += row.count();
                }
                return text.append("items=").append(total).append('\n').toString();
            }
            StringBuilder text = new StringBuilder("name,count\n");
            long total = 0;
            for (Row row : rows) {
                text.append(quoteLabel(row.label())).append(delimiter).append(row.count()).append('\n');
                total += row.count();
            }
            return text.append("total").append(delimiter).append(total).append('\n').toString();
        }
    }
    public static String render(Format format, List<Row> rows) {
        Objects.requireNonNull(format, "format");
        Objects.requireNonNull(rows, "rows");
        for (Row row : rows) Objects.requireNonNull(row, "row");
        return new GeneralOutput().render(format, List.copyOf(rows));
    }
}
