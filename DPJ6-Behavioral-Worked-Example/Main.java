import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Main {
    @FunctionalInterface public interface LabelStyle { String apply(String text); }
    public static String styled(LabelStyle style, String text) {
        return Objects.requireNonNull(Objects.requireNonNull(style, "style").apply(Objects.requireNonNull(text, "text")), "styled text");
    }
    public static final class Gate {
        private State state = Phase.CLOSED;
        public String phase() { return state.name(); }
        public void open() { state.open(this); }
        public void lock() { state.lock(this); }
        private interface State {
            String name();
            default void open(Gate gate) { throw new IllegalStateException("open unavailable"); }
            default void lock(Gate gate) { throw new IllegalStateException("lock unavailable"); }
        }
        private enum Phase implements State {
            CLOSED { public void open(Gate gate) { gate.state = OPEN; } },
            OPEN { public void lock(Gate gate) { gate.state = LOCKED; } },
            LOCKED
        }
    }
    public abstract static class ListPrinter {
        protected abstract String item(String text);
        public final String print(List<String> values) {
            List<String> snapshot = List.copyOf(values), output = new ArrayList<>();
            for (String value : snapshot) output.add(Objects.requireNonNull(item(value), "item text"));
            return "[" + String.join("|", output) + "]";
        }
    }
    public static final class DashPrinter extends ListPrinter {
        protected String item(String text) { return "-" + text; }
    }
    public record Labels(List<String> values) implements Iterable<String> {
        public Labels { values = List.copyOf(values); }
        public Iterator<String> iterator() { return values.iterator(); }
    }
    public static void main(String[] args) {
        Gate gate = new Gate(); gate.open(); gate.lock();
        System.out.println(gate.phase());
        System.out.println(styled(text -> text.toUpperCase(Locale.ROOT), "ready"));
        System.out.println(new DashPrinter().print(List.of("one","two")));
        for (String label : new Labels(List.of("a","b"))) System.out.println(label);
    }
}
