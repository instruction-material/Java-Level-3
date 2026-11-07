import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        equal(new Main.Greeting(new Main.Plain(), "Ready").render(), "Ready");
        equal(new Main.Greeting(new Main.Upper(), "Ready").render(), "READY");
        equal(new Main.Warning(new Main.Plain(), "wait").render(), "Warning: wait");
        equal(new Main.Warning(new Main.Upper(), "wait").render(), "WARNING: WAIT");
        Locale saved = Locale.getDefault();
        try { Locale.setDefault(Locale.forLanguageTag("tr-TR")); equal(new Main.Greeting(new Main.Upper(), "i").render(), "I"); }
        finally { Locale.setDefault(saved); }
        Main.Node a = () -> "a", b = () -> "b";
        List<Main.Node> input = new ArrayList<>(List.of(a,b));
        Main.Sequence sequence = new Main.Sequence(input);input.clear();
        equal(sequence.render(), "[a,b]"); yes(sequence.children().get(0) == a);
        fails(UnsupportedOperationException.class, () -> sequence.children().clear());
        equal(new Main.Sequence(List.of()).render(), "[]");
        equal(new Main.Sequence(List.of(sequence)).render(), "[[a,b]]");
        equal(new Main.Emphasis(sequence).render(), "*[a,b]*");
        equal(new Main.Sequence(List.of(new Main.Emphasis(a),b)).render(), "[*a*,b]");
        int[] calls = {0};
        equal(new Main.Emphasis(() -> { calls[0]++; return "x"; }).render(), "*x*"); equal(calls[0],1);
        int[] loads = {0}, renders = {0};
        Main.LazyNode lazy = new Main.LazyNode(() -> { loads[0]++; return () -> "x" + renders[0]++; });
        yes(!lazy.loaded()); equal(loads[0],0); equal(lazy.render(),"x0"); yes(lazy.loaded());
        equal(lazy.render(),"x1"); equal(loads[0],1); equal(renders[0],2);
        int[] attempts = {0};
        Main.LazyNode recovering = new Main.LazyNode(() -> attempts[0]++ == 0 ? null : () -> "ok");
        fails(NullPointerException.class,recovering::render);yes(!recovering.loaded());
        equal(recovering.render(),"ok");equal(attempts[0],2);
        fails(NullPointerException.class, () -> new Main.Greeting(null,"x"));
        fails(NullPointerException.class, () -> new Main.Greeting(value -> null,"x").render());
        fails(NullPointerException.class, () -> new Main.Sequence(List.of(() -> null)).render());
        fails(NullPointerException.class, () -> new Main.Emphasis(null));
        fails(NullPointerException.class, () -> new Main.LazyNode(null));
        Main.WordPool pool = new Main.WordPool();
        Main.Word first = pool.get(Main.Lexeme.READY), second = pool.get(Main.Lexeme.READY);
        yes(first == second);equal(first,new Main.Word("ready"));
        Main.At left = new Main.At(first,1),right = new Main.At(second,2);
        equal(left.render(),"ready@1");equal(right.render(),"ready@2");yes(left.word() == right.word());
        Main.Word separate = new Main.WordPool().get(Main.Lexeme.READY);
        equal(first,separate);yes(first != separate);
        equal(pool.get(Main.Lexeme.WAIT).text(),"wait");
        fails(NullPointerException.class, () -> pool.get(null));
        fails(IllegalArgumentException.class, () -> new Main.At(first,-1));
        fails(IllegalArgumentException.class, () -> new Main.At(first,10));
        System.out.println("Structural worked contracts: " + checks + " passed");
    }
}
