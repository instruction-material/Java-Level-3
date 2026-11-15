package reports;
import java.util.List;
import java.util.Objects;
public final class ReportService {
    private ReportService() {}
    public static String render(Format format, List<Row> rows) {
        Objects.requireNonNull(format, "format");
        Report report = switch (format) {
            case PLAIN -> new PlainReport();
            case CSV -> new CsvReport();
        };
        return report.render(rows);
    }
}
