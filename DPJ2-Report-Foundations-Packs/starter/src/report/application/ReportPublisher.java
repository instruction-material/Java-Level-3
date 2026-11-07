package report.application;
import java.util.Objects;
import report.domain.Report;
import report.ports.ReportFormatter;
import report.ports.ReportSink;
public final class ReportPublisher {
    private final ReportFormatter formatter;
    private final ReportSink sink;
    public ReportPublisher(ReportFormatter formatter, ReportSink sink) {
        this.formatter = Objects.requireNonNull(formatter, "formatter");
        this.sink = Objects.requireNonNull(sink, "sink");
    }
    public void publish(Report report) {
        throw new UnsupportedOperationException("Implement one format call followed by one sink write.");
    }
}
