import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class Main {
    private static int bounded(int value) {
        if (value < 0 || value > 9) throw new IllegalArgumentException("value must be 0..9");
        return value;
    }
    public static final class Counter {
        private int value;
        public Counter(int value) { this.value = bounded(value); }
        public int value() { return value; }
        public void set(int value) { this.value = bounded(value); }
        public static final class Snapshot {
            private final Counter owner;
            private final int value;
            private Snapshot(Counter owner, int value) { this.owner=owner; this.value=value; }
        }
        public Snapshot save() { return new Snapshot(this,value); }
        public void restore(Snapshot snapshot) {
            Objects.requireNonNull(snapshot,"snapshot");
            if (snapshot.owner != this) throw new IllegalArgumentException("foreign snapshot");
            value=snapshot.value;
        }
    }
    public record Notice(int before, int after) {
        public Notice { bounded(before); bounded(after); }
    }
    public static final class Notices {
        @FunctionalInterface public interface Token extends AutoCloseable { @Override void close(); }
        private record Entry(Consumer<Notice> action, Object identity) {}
        private final List<Entry> entries=new ArrayList<>();
        public Token subscribe(Consumer<Notice> listener) {
            Entry entry=new Entry(Objects.requireNonNull(listener,"listener"),new Object());
            entries.add(entry); return () -> entries.remove(entry);
        }
        public void publish(Notice notice) {
            Objects.requireNonNull(notice,"notice");
            for (Entry entry : List.copyOf(entries)) entry.action().accept(notice);
        }
    }
    public static final class Change {
        private final Counter counter;
        private final int after;
        private Counter.Snapshot before;
        private boolean executed,undone;
        public Change(Counter counter,int after) { this.counter=Objects.requireNonNull(counter,"counter"); this.after=bounded(after); }
        public void execute() {
            if (executed) throw new IllegalStateException("already executed");
            before=counter.save(); counter.set(after); executed=true;
        }
        public void undo() {
            if (!executed || undone || counter.value()!=after) throw new IllegalStateException("undo unavailable");
            counter.restore(before); undone=true;
        }
    }
    public static final class Coordinator {
        private final Counter counter=new Counter(0);
        private final List<IntPredicate> gates;
        private final Notices notices;
        private final Deque<Change> history=new ArrayDeque<>();
        private boolean busy;
        public Coordinator(List<IntPredicate> gates,Notices notices) {
            this.gates=List.copyOf(gates); this.notices=Objects.requireNonNull(notices,"notices");
        }
        public int value() { return counter.value(); }
        public int depth() { return history.size(); }
        private void enter() { if (busy) throw new IllegalStateException("reentrant operation"); busy=true; }
        public boolean request(int value) {
            enter();
            try {
                bounded(value);
                for (IntPredicate gate : gates) if (!gate.test(value)) return false;
                int before=counter.value(); Change command=new Change(counter,value);
                command.execute(); history.push(command);
                notices.publish(new Notice(before,counter.value())); return true;
            } finally { busy=false; }
        }
        public boolean undo() {
            enter();
            try {
                if (history.isEmpty()) return false;
                int before=counter.value(); history.peek().undo(); history.pop();
                notices.publish(new Notice(before,counter.value())); return true;
            } finally { busy=false; }
        }
    }
    public sealed interface Expr permits Value, Add { <R> R accept(Visitor<R> visitor); }
    public interface Visitor<R> { R value(Value node); R add(Add node); }
    public record Value(int number) implements Expr {
        public Value { bounded(number); }
        public <R> R accept(Visitor<R> visitor) { return Objects.requireNonNull(Objects.requireNonNull(visitor,"visitor").value(this),"result"); }
    }
    public record Add(Expr left,Expr right) implements Expr {
        public Add { Objects.requireNonNull(left,"left"); Objects.requireNonNull(right,"right"); }
        public <R> R accept(Visitor<R> visitor) { return Objects.requireNonNull(Objects.requireNonNull(visitor,"visitor").add(this),"result"); }
    }
    public static final class Sum implements Visitor<Integer> {
        public Integer value(Value node) { return node.number(); }
        public Integer add(Add node) { return Math.addExact(node.left().accept(this),node.right().accept(this)); }
    }
    public static final class Show implements Visitor<String> {
        public String value(Value node) { return Integer.toString(node.number()); }
        public String add(Add node) { return "("+node.left().accept(this)+"+"+node.right().accept(this)+")"; }
    }
    public static void main(String[] args) {
        Notices notices=new Notices(); notices.subscribe(n -> System.out.println(n.before()+"->"+n.after()));
        Coordinator coordinator=new Coordinator(List.of(value -> value>0,value -> value<=5),notices);
        System.out.println(coordinator.request(2)); System.out.println(coordinator.request(0));
        System.out.println(coordinator.undo());
        Expr expression=new Add(new Value(2),new Value(3));
        System.out.println(expression.accept(new Show())); System.out.println(expression.accept(new Sum()));
    }
}
