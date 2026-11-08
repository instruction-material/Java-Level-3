package notebook;
import notebook.api.AuditSink;
import notebook.api.Notebook;

public final class Composition {
    private Composition() {}
    public static Notebook create(AuditSink audit) { return new NotebookDesk(audit); }
}
