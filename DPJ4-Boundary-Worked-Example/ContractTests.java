import java.util.ArrayList;
import java.util.List;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        if (!actual.equals(expected)) throw new AssertionError("expected " + expected + ", got " + actual);
        checks++;
    }
    private static void yes(boolean condition) {
        if (!condition) throw new AssertionError("expected true");
        checks++;
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (!type.isInstance(error)) throw new AssertionError("wrong failure", error);
            checks++; return;
        }
        throw new AssertionError("missing " + type.getSimpleName());
    }
    public static void main(String[] args) {
        List<String> input = new ArrayList<>(List.of("Ready"));
        Main.Draft original = new Main.Draft("T", input); input.add("Caller");
        equal(original.snapshot().lines(), List.of("Ready"));
        Main.Draft copy = original.copy(); yes(copy != original); copy.add("Next");
        equal(copy.snapshot().lines(), List.of("Ready", "Next"));
        equal(original.snapshot().lines(), List.of("Ready"));
        Main.Note frozen = copy.snapshot(); copy.add("Later");
        equal(frozen.lines(), List.of("Ready", "Next"));
        fails(UnsupportedOperationException.class, () -> frozen.lines().add("bad"));
        fails(NullPointerException.class, () -> copy.add(null));
        fails(IllegalArgumentException.class, () -> copy.add("bad\nline"));
        int[] calls = {0};
        Main.Adapter adapter = new Main.Adapter(() -> { calls[0]++; return "T|Ready|"; });
        equal(adapter.load(), new Main.Note("T", List.of("Ready", ""))); equal(calls[0], 1);
        equal(new Main.Adapter(() -> "T").load(), new Main.Note("T", List.of()));
        fails(NullPointerException.class, () -> new Main.Adapter(null));
        fails(NullPointerException.class, () -> new Main.Adapter(() -> null).load());
        fails(IllegalArgumentException.class, () -> new Main.Adapter(() -> "|Body").load());
        fails(IllegalArgumentException.class, () -> new Main.Adapter(() -> "T|bad\rline").load());
        fails(IllegalStateException.class, () -> new Main.Adapter(() -> { throw new IllegalStateException("unavailable"); }).load());
        int[] loaded = {0};
        Main.Facade facade = new Main.Facade(() -> { loaded[0]++; return new Main.Note("T", List.of("Ready", "")); });
        equal(facade.render(), "T\nReady\n\n"); equal(loaded[0], 1);
        fails(NullPointerException.class, () -> new Main.Facade(null));
        fails(NullPointerException.class, () -> new Main.Facade(() -> null).render());
        fails(IllegalStateException.class, () -> new Main.Facade(() -> { throw new IllegalStateException("unavailable"); }).render());
        equal(new Main.Facade(() -> new Main.Note("Other", List.of())).render(), "Other\n");
        equal(Main.LineEnding.INSTANCE.value(), "\n"); yes(Main.LineEnding.INSTANCE == Main.LineEnding.INSTANCE);
        System.out.println("Boundary worked contracts: " + checks + " passed");
    }
}
