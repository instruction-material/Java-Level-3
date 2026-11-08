import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import notebook.Composition;
import notebook.api.Action;
import notebook.api.AuditSink;
import notebook.api.Note;
import notebook.api.Notebook;
import notebook.legacy.LegacyNotebook;

public final class ContractTests {
    private static int checks;
    private ContractTests() {}
    private static void same(Object expected, Object actual) {
        checks++;
        if (!Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    private static void raises(Class<? extends Throwable> expected, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable failure) {
            if (expected.isInstance(failure)) return;
            throw new AssertionError("Wrong failure: " + failure, failure);
        }
        throw new AssertionError("Missing " + expected.getSimpleName());
    }
    private static void core(Function<AuditSink, Notebook> make) {
        List<String> audit = new ArrayList<>();
        Notebook book = make.apply((a, n) -> audit.add(a + "|" + n.id() + "|" + n.version()));
        same(List.of(), book.all());
        same(false, book.find("absent").isPresent());
        Note first = book.create("plan", "Read");
        same(new Note("plan", "Read", 1), first);
        same(List.of("CREATED|plan|1"), audit);
        List<Note> snapshot = book.all();
        raises(UnsupportedOperationException.class, () -> snapshot.add(first));
        same(true, book.edit("plan", 1, "Sketch"));
        same(new Note("plan", "Sketch", 2), book.find("plan").orElseThrow());
        same("Read", snapshot.getFirst().text());
        same(false, book.edit("plan", 1, "Stale"));
        same(false, book.edit("missing", 1, "Unused"));
        same(List.of("CREATED|plan|1", "EDITED|plan|2"), audit);
        raises(IllegalStateException.class, () -> book.create("plan", "Duplicate"));
        same(2L, book.find("plan").orElseThrow().version());
        same(true, book.edit("plan", 2, "Sketch"));
        same(3L, book.find("plan").orElseThrow().version());
        book.create("second", "");
        same(List.of("plan", "second"), book.all().stream().map(Note::id).toList());
        same(1L, book.find("second").orElseThrow().version());
        String emoji80 = "😀".repeat(80);
        same(emoji80, book.create("unicode", emoji80).text());
        same(1L, book.create("a" + "9".repeat(15), " ").version());
        int before = audit.size();
        for (String id : List.of("", "A", "1a", "-a", "a b", "a".repeat(17))) {
            raises(IllegalArgumentException.class, () -> book.create(id, "x"));
            raises(IllegalArgumentException.class, () -> book.find(id));
            raises(IllegalArgumentException.class, () -> book.edit(id, 1, "x"));
        }
        raises(NullPointerException.class, () -> book.create(null, "x"));
        raises(NullPointerException.class, () -> book.find(null));
        raises(NullPointerException.class, () -> book.edit(null, 1, "x"));
        for (String text : List.of("x".repeat(81), "😀".repeat(81), "a\nb", "a\rb")) {
            raises(IllegalArgumentException.class, () -> book.create("invalid", text));
            raises(IllegalArgumentException.class, () -> book.edit("missing", 1, text));
        }
        raises(NullPointerException.class, () -> book.create("null", null));
        raises(NullPointerException.class, () -> book.edit("missing", 1, null));
        for (long version : new long[] {0, -1, Long.MIN_VALUE}) {
            raises(IllegalArgumentException.class, () -> book.edit("missing", version, ""));
        }
        same(before, audit.size());
        same(false, book.find("invalid").isPresent());
        same(4, book.all().size());
        Notebook independent = make.apply((a, n) -> {});
        same(List.of(), independent.all());
        RuntimeException marker = new IllegalStateException("audit failed");
        Notebook failing = make.apply((a, n) -> { throw marker; });
        checks++;
        try { failing.create("saved", "Committed"); throw new AssertionError("Missing audit failure"); }
        catch (RuntimeException failure) { if (failure != marker) throw new AssertionError("Failure identity"); }
        same(new Note("saved", "Committed", 1), failing.find("saved").orElse(null));
        checks++;
        try { failing.edit("saved", 1, "Changed"); throw new AssertionError("Missing edit audit failure"); }
        catch (RuntimeException failure) { if (failure != marker) throw new AssertionError("Failure identity"); }
        same(new Note("saved", "Changed", 2), failing.find("saved").orElse(null));
        raises(NullPointerException.class, () -> make.apply(null));
    }
    private static void optional() {
        List<String> audit = new ArrayList<>();
        Notebook book = Composition.create((a, n) -> audit.add(a + "|" + n.id() + "|" + n.version()));
        book.create("first", "A");
        book.create("second", "B");
        List<Note> snapshot = book.all();
        same(false, book.remove("absent", 1));
        same(false, book.remove("first", 2));
        same(2, audit.size());
        raises(IllegalArgumentException.class, () -> book.remove("first", 0));
        raises(IllegalArgumentException.class, () -> book.remove("Invalid", 1));
        raises(NullPointerException.class, () -> book.remove(null, 1));
        same(true, book.remove("first", 1));
        same(false, book.find("first").isPresent());
        same(List.of(new Note("second", "B", 1)), book.all());
        same(2, snapshot.size());
        same(false, book.remove("first", 1));
        same("REMOVED|first|1", audit.getLast());
        same(3, audit.size());
        same(new Note("first", "Fresh", 1), book.create("first", "Fresh"));
        same(List.of("second", "first"), book.all().stream().map(Note::id).toList());
        RuntimeException marker = new IllegalArgumentException("sink");
        Notebook failing = Composition.create((a, n) -> { if (a == Action.REMOVED) throw marker; });
        failing.create("gone", "");
        checks++;
        try { failing.remove("gone", 1); throw new AssertionError("Missing removal audit failure"); }
        catch (RuntimeException failure) { if (failure != marker) throw new AssertionError("Failure identity"); }
        same(false, failing.find("gone").isPresent());
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!List.of("base", "core", "all").contains(mode)) throw new IllegalArgumentException("mode");
        core(LegacyNotebook::new);
        if (!mode.equals("base")) core(Composition::create);
        if (mode.equals("all")) optional();
        System.out.println((mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core" : "Complete")
            + " architecture studio contracts: " + checks + " passed");
    }
}
