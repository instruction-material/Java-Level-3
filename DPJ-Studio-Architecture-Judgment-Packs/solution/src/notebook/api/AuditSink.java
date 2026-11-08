package notebook.api;

@FunctionalInterface
public interface AuditSink {
    void record(Action action, Note note);
}
