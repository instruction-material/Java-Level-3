package report.adapters;
import java.util.Objects;
import report.ports.ReportSink;

public final class ConsoleSink implements ReportSink {
    @Override
    public void write(String text) { System.out.print(Objects.requireNonNull(text, "text")); }
}
