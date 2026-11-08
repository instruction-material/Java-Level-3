package notebook;

import notebook.api.AuditSink;
import notebook.api.Notebook;
import notebook.application.NotebookService;
import notebook.memory.MemoryNotes;

public final class Composition {
    private Composition() {}
    public static Notebook create(AuditSink audit) {
        return new NotebookService(new MemoryNotes(), audit);
    }
}
