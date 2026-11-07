import java.util.function.Supplier;

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
        Main.Label label = new Main.Label("Ready");
        equal(label, Main.Label.named("Ready"));
        int[] calls = {0};
        Supplier<Main.Label> cached = () -> { calls[0]++; return label; };
        yes(Main.supplied(cached) == label); yes(Main.supplied(cached) == label);
        equal(calls[0], 2);
        fails(NullPointerException.class, () -> Main.supplied(null));
        fails(NullPointerException.class, () -> Main.supplied(() -> null));
        fails(NullPointerException.class, () -> Main.Label.named(null));
        equal(new Main.Brackets().display("Ready"), "[Ready]");
        int[] created = {0};
        Main.Creator custom = new Main.Creator() {
            protected Main.Token create(String text) { created[0]++; return new Main.PlainToken(text); }
        };
        equal(custom.display("Ready"), "Ready"); equal(created[0], 1);
        fails(NullPointerException.class, () -> custom.display(null)); equal(created[0], 1);
        fails(NullPointerException.class, () -> new Main.Creator() {
            protected Main.Token create(String text) { return null; }
        }.display("Ready"));
        fails(NullPointerException.class, () -> new Main.Creator() {
            protected Main.Token create(String text) { return () -> null; }
        }.display("Ready"));
        equal(Main.themed(new Main.PlainTheme(), "T", "B"), "T\nB");
        equal(Main.themed(new Main.MarkedTheme(), "T", "B"), "**T**\n> B");
        equal(new Main.MarkedTheme().heading().render("T"), "**T**");
        equal(new Main.MarkedTheme().body().render("B"), "> B");
        fails(NullPointerException.class, () -> Main.themed(null, "T", "B"));
        fails(NullPointerException.class, () -> Main.themed(new Main.PlainTheme(), null, "B"));
        Main.PageBuilder builder = new Main.PageBuilder("T");
        equal(builder.build(), new Main.Page("T", "", false));
        yes(builder.body("B") == builder); yes(builder.emphasize(true) == builder);
        Main.Page first = builder.build();
        equal(first, new Main.Page("T", "B", true)); equal(first.render(), "[T]\nB");
        builder.body("Next").emphasize(false);
        equal(first.render(), "[T]\nB"); equal(builder.build().render(), "T\nNext");
        yes(builder.build() != builder.build());
        fails(NullPointerException.class, () -> new Main.PageBuilder(null));
        fails(NullPointerException.class, () -> builder.body(null));
        fails(NullPointerException.class, () -> new Main.Page("T", null, false));
        System.out.println("Creation worked contracts: " + checks + " passed");
    }
}
