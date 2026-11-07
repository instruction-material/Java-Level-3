import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual, Object expected) {
        checks++;
        if (!Objects.equals(actual, expected)) throw new AssertionError(actual + " != " + expected);
    }
    private static void fails(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError(error);
        }
        throw new AssertionError("Expected " + type.getName());
    }
    public static void main(String[] args) {
        equal(Main.styled(String::strip, " a "), "a");
        equal(Main.styled(text -> ">" + text, "a"), ">a");
        fails(NullPointerException.class, () -> Main.styled(null, "a"));
        fails(NullPointerException.class, () -> Main.styled(String::strip, null));
        fails(NullPointerException.class, () -> Main.styled(text -> null, "a"));
        fails(IllegalStateException.class, () -> Main.styled(text -> { throw new IllegalStateException(); }, "a"));
        Main.Gate gate = new Main.Gate(), separate = new Main.Gate();
        equal(gate.phase(),"CLOSED"); fails(IllegalStateException.class,gate::lock);
        equal(gate.phase(),"CLOSED"); gate.open(); equal(gate.phase(),"OPEN");
        equal(separate.phase(),"CLOSED"); fails(IllegalStateException.class,gate::open);
        gate.lock(); equal(gate.phase(),"LOCKED");
        fails(IllegalStateException.class,gate::open); fails(IllegalStateException.class,gate::lock);
        equal(new Main.DashPrinter().print(List.of("a","b")),"[-a|-b]");
        equal(new Main.DashPrinter().print(List.of()),"[]");
        List<String> source = new ArrayList<>(List.of("a","b")), calls = new ArrayList<>();
        Main.ListPrinter custom = new Main.ListPrinter() {
            protected String item(String text) { calls.add(text); source.clear(); return text; }
        };
        equal(custom.print(source),"[a|b]"); equal(calls,List.of("a","b")); calls.clear();
        fails(NullPointerException.class, () -> custom.print(Arrays.asList("a",null))); equal(calls,List.of());
        fails(NullPointerException.class, () -> custom.print(null)); equal(calls,List.of());
        Main.ListPrinter nullItem = new Main.ListPrinter() { protected String item(String text) { return null; } };
        fails(NullPointerException.class, () -> nullItem.print(List.of("a")));
        List<String> original = new ArrayList<>(List.of("a","a","b"));
        Main.Labels labels = new Main.Labels(original); original.clear();
        equal(labels.values(),List.of("a","a","b"));
        fails(UnsupportedOperationException.class, () -> labels.values().add("c"));
        Iterator<String> first = labels.iterator(), second = labels.iterator();
        equal(first.hasNext(),true); equal(first.hasNext(),true); equal(first.next(),"a"); equal(second.next(),"a");
        List<String> rest = new ArrayList<>(); first.forEachRemaining(rest::add); equal(rest,List.of("a","b"));
        equal(first.hasNext(),false); fails(NoSuchElementException.class,first::next);
        fails(UnsupportedOperationException.class,second::remove);
        fails(NullPointerException.class, () -> new Main.Labels(null));
        fails(NullPointerException.class, () -> new Main.Labels(Arrays.asList("a",null)));
        System.out.println("Behavioral worked contracts: " + checks + " passed");
    }
}
