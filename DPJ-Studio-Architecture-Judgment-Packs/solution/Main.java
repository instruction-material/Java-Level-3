import notebook.Composition;
import notebook.api.Action;
import notebook.api.AuditSink;
import notebook.api.Notebook;
import notebook.legacy.LegacyNotebook;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        AuditSink audit = (action, note) ->
            System.out.println(action + "|" + note.id() + "|" + note.version());
        Notebook book = args.length > 0 && args[0].equals("base")
            ? new LegacyNotebook(audit) : Composition.create(audit);
        book.create("plan", "Read");
        System.out.println(book.edit("plan", 1, "Sketch"));
        System.out.println(book.edit("plan", 1, "Stale"));
        System.out.println(book.find("plan").orElseThrow().text());
        System.out.println(book.all().size());
    }
}
