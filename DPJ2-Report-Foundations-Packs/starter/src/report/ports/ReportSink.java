package report.ports;
@FunctionalInterface
public interface ReportSink {
    /** Consume one complete non-null formatted document or propagate a failure. */
    void write(String text);
}
