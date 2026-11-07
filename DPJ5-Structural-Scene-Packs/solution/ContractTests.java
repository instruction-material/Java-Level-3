import java.util.ArrayList;
import java.util.List;
import scene.*;

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
    private static void core() {
        Renderer plain = new Formats.Plain(), compact = new Formats.Compact();
        for (int r = 1; r <= 20; r++) {
            equal(new Circle(plain, "x", r).render(), "circle(x," + r + ")");
            equal(new Circle(compact, "x", r).render(), "C:x:" + r);
        }
        for (int w = 1; w <= 10; w++) for (int h = 1; h <= 10; h++) {
            equal(new Box(plain, "x", w, h).render(), "box(x," + w + "x" + h + ")");
            equal(new Box(compact, "x", w, h).render(), "B:x:" + w + ":" + h);
        }
        Graphic circle = new Circle(plain, "c", 2), box = new Box(compact, "b", 3, 4);
        List<Graphic> supplied = new ArrayList<>(List.of(circle, box));
        Group group = new Group(plain, supplied); supplied.clear();
        equal(group.render(), "group[circle(c,2);B:b:3:4]");
        yes(group.children().get(0) == circle); yes(group.children().get(1) == box);
        fails(UnsupportedOperationException.class, () -> group.children().clear());
        equal(new Group(plain, List.of()).render(), "group[]");
        equal(new Group(compact, List.of()).render(), "G()");
        equal(new Group(compact, List.of(group)).render(), "G(group[circle(c,2);B:b:3:4])");
        Frame frame = new Frame(circle);
        yes(frame.wrapped() == circle); yes(frame != circle);
        equal(frame.render(), "[circle(c,2)]");
        equal(new Frame(frame).render(), "[[circle(c,2)]]");
        equal(new Frame(group).render(), "[group[circle(c,2);B:b:3:4]]");
        equal(new Group(plain, List.of(frame)).render(), "group[[circle(c,2)]]");
        int[] childCalls = {0};
        Graphic tracked = () -> { childCalls[0]++; return "X"; };
        equal(new Frame(tracked).render(), "[X]"); equal(childCalls[0], 1);
        childCalls[0] = 0;
        equal(new Group(plain, List.of(tracked, tracked)).render(), "group[X;X]");
        equal(childCalls[0], 2);
        fails(NullPointerException.class, () -> new Circle(null, "x", 1));
        fails(NullPointerException.class, () -> new Group(null, List.of()));
        fails(NullPointerException.class, () -> new Group(plain, null));
        fails(NullPointerException.class, () -> new Group(plain, java.util.Arrays.asList((Graphic) null)));
        fails(NullPointerException.class, () -> new Frame(null));
        fails(NullPointerException.class, () -> new Frame(() -> null).render());
        fails(NullPointerException.class, () -> new Group(plain, List.of(() -> null)).render());
        for (String name : List.of("", "x y", "bad\nline", "1x"))
            fails(IllegalArgumentException.class, () -> new Circle(plain, name, 1));
        for (int size : new int[]{0, -1, 101}) {
            fails(IllegalArgumentException.class, () -> new Circle(plain, "x", size));
            fails(IllegalArgumentException.class, () -> new Box(plain, "x", size, 1));
        }
        Renderer nullRenderer = new Renderer() {
            public String circle(String name, int radius) { return null; }
            public String box(String name, int width, int height) { return null; }
            public String group(List<String> children) { return null; }
        };
        fails(NullPointerException.class, () -> new Circle(nullRenderer, "x", 1).render());
        fails(NullPointerException.class, () -> new Box(nullRenderer, "x", 1, 1).render());
        fails(NullPointerException.class, () -> new Group(nullRenderer, List.of()).render());
        int[] loads = {0}, renders = {0};
        LazyGraphic lazy = new LazyGraphic(() -> { loads[0]++; return () -> "value" + renders[0]++; });
        yes(!lazy.isLoaded()); equal(loads[0], 0);
        equal(lazy.render(), "value0"); yes(lazy.isLoaded());
        equal(lazy.render(), "value1"); equal(loads[0], 1); equal(renders[0], 2);
        int[] attempts = {0};
        LazyGraphic recovering = new LazyGraphic(() -> {
            if (attempts[0]++ == 0) throw new IllegalStateException("load failed");
            return () -> "ok";
        });
        fails(IllegalStateException.class, recovering::render); yes(!recovering.isLoaded()); equal(attempts[0], 1);
        equal(recovering.render(), "ok"); equal(attempts[0], 2);
        int[] nullLoads = {0};
        LazyGraphic nullFirst = new LazyGraphic(() -> nullLoads[0]++ == 0 ? null : () -> "ok");
        fails(NullPointerException.class, nullFirst::render); yes(!nullFirst.isLoaded());
        equal(nullFirst.render(), "ok"); equal(nullLoads[0], 2);
        int[] created = {0}, failedRenders = {0};
        LazyGraphic renderFailure = new LazyGraphic(() -> {
            created[0]++;
            return () -> { if (failedRenders[0]++ == 0) throw new IllegalStateException("render failed"); return "ok"; };
        });
        fails(IllegalStateException.class, renderFailure::render); yes(renderFailure.isLoaded());
        equal(renderFailure.render(), "ok"); equal(created[0], 1);
        fails(NullPointerException.class, () -> new LazyGraphic(null));
        fails(NullPointerException.class, () -> new LazyGraphic(() -> () -> null).render());
        GlyphPool pool = new GlyphPool();
        for (char symbol = 'A'; symbol <= 'Z'; symbol++) for (GlyphPool.Tone tone : GlyphPool.Tone.values()) {
            GlyphPool.Glyph glyph = pool.get(symbol, tone);
            yes(glyph == pool.get(symbol, tone)); equal(glyph.symbol(), symbol); equal(glyph.tone(), tone);
            Placement first = new Placement(glyph, 1, 2), second = new Placement(glyph, 5, 6);
            yes(first.glyph() == second.glyph());
            equal(first.render(), symbol + ":" + tone + "@1,2");
            equal(second.render(), symbol + ":" + tone + "@5,6");
        }
        equal(pool.size(), 52);
        GlyphPool.Glyph a = pool.get('A', GlyphPool.Tone.INK);
        GlyphPool.Glyph otherPool = new GlyphPool().get('A', GlyphPool.Tone.INK);
        yes(a != otherPool); equal(a, otherPool);
        fails(NullPointerException.class, () -> pool.get('A', null));
        for (char bad : new char[]{'@', '[', 'a'}) fails(IllegalArgumentException.class, () -> pool.get(bad, GlyphPool.Tone.INK));
        fails(NullPointerException.class, () -> new Placement(null, 0, 0));
        fails(IllegalArgumentException.class, () -> new Placement(a, -1, 0));
        fails(IllegalArgumentException.class, () -> new Placement(a, 0, 1001));
    }
    private static void extension() {
        GlyphPool.Glyph glyph = new GlyphPool().get('A', GlyphPool.Tone.INK);
        Placement original = new Placement(glyph, 10, 20);
        Placement moved = original.shift(3, -4);
        equal(moved, new Placement(glyph, 13, 16)); equal(original, new Placement(glyph, 10, 20));
        yes(moved.glyph() == original.glyph()); yes(original.shift(0, 0) != original);
        fails(IllegalArgumentException.class, () -> original.shift(-11, 0));
        fails(IllegalArgumentException.class, () -> original.shift(0, 981));
        fails(ArithmeticException.class, () -> original.shift(Integer.MAX_VALUE, 0));
    }
    public static void main(String[] args) {
        boolean coreOnly = args.length == 1 && args[0].equals("core");
        if (!(coreOnly || args.length == 0)) throw new IllegalArgumentException("mode must be core or empty");
        core(); if (!coreOnly) extension();
        System.out.println((coreOnly ? "Core structural" : "Complete structural") + " contracts: " + checks + " passed");
    }
}
