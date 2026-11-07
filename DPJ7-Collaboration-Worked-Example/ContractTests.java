import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++; if (!Objects.equals(actual,expected)) throw new AssertionError(actual+" != "+expected);
    }
    private static void fails(Class<? extends Throwable> type,Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError(error);
        }
        throw new AssertionError("Expected "+type.getName());
    }
    public static void main(String[] args) {
        Main.Counter counter=new Main.Counter(2); Main.Counter.Snapshot saved=counter.save();
        counter.set(3); counter.restore(saved); equal(counter.value(),2);
        fails(IllegalArgumentException.class, () -> counter.restore(new Main.Counter(2).save()));
        fails(NullPointerException.class, () -> counter.restore(null));
        fails(IllegalArgumentException.class, () -> counter.set(10)); equal(counter.value(),2);
        Main.Change change=new Main.Change(counter,4); fails(IllegalStateException.class,change::undo);
        change.execute(); equal(counter.value(),4); fails(IllegalStateException.class,change::execute);
        counter.set(5); fails(IllegalStateException.class,change::undo); equal(counter.value(),5);
        counter.set(4); change.undo(); equal(counter.value(),2); fails(IllegalStateException.class,change::undo);
        fails(IllegalStateException.class,change::execute);
        Main.Notices notices=new Main.Notices(); List<String> seen=new ArrayList<>();
        Consumer<Main.Notice> observer=n -> seen.add(n.before()+"->"+n.after());
        Main.Notices.Token a=notices.subscribe(observer),b=notices.subscribe(observer);
        notices.publish(new Main.Notice(1,2)); equal(seen,List.of("1->2","1->2"));
        a.close(); a.close(); seen.clear(); notices.publish(new Main.Notice(2,3)); equal(seen,List.of("2->3")); b.close();
        fails(NullPointerException.class, () -> notices.subscribe(null));
        fails(NullPointerException.class, () -> notices.publish(null));
        List<String> calls=new ArrayList<>();
        Main.Coordinator editor=new Main.Coordinator(List.of(v -> { calls.add("positive"); return v>0; },
            v -> { calls.add("small"); return v<=5; }),new Main.Notices());
        equal(editor.request(0),false); equal(calls,List.of("positive")); equal(editor.value(),0); equal(editor.depth(),0);
        calls.clear(); equal(editor.request(2),true); equal(calls,List.of("positive","small"));
        equal(editor.request(3),true); equal(editor.depth(),2); equal(editor.undo(),true); equal(editor.value(),2);
        equal(editor.undo(),true); equal(editor.value(),0); equal(editor.undo(),false);
        fails(IllegalArgumentException.class, () -> editor.request(10)); equal(editor.depth(),0);
        List<IntPredicate> rules=new ArrayList<>(List.of(v -> true));
        Main.Coordinator copied=new Main.Coordinator(rules,new Main.Notices()); rules.clear(); rules.add(v -> false);
        equal(copied.request(1),true);
        Main.Notices throwing=new Main.Notices(); Main.Coordinator committed=new Main.Coordinator(List.of(),throwing);
        Main.Notices.Token bad=throwing.subscribe(n -> { throw new UnsupportedOperationException(); });
        fails(UnsupportedOperationException.class, () -> committed.request(2)); equal(committed.value(),2); equal(committed.depth(),1);
        bad.close(); equal(committed.undo(),true); equal(committed.value(),0);
        Main.Notices guardedBus=new Main.Notices(); Main.Coordinator guarded=new Main.Coordinator(List.of(),guardedBus);
        guardedBus.subscribe(n -> { fails(IllegalStateException.class, () -> guarded.request(3)); fails(IllegalStateException.class,guarded::undo); });
        equal(guarded.request(2),true); equal(guarded.depth(),1);
        Main.Expr expression=new Main.Add(new Main.Value(2),new Main.Add(new Main.Value(3),new Main.Value(4)));
        equal(expression.accept(new Main.Show()),"(2+(3+4))"); equal(expression.accept(new Main.Sum()),9);
        fails(NullPointerException.class, () -> expression.accept(null));
        fails(NullPointerException.class, () -> new Main.Add(null,new Main.Value(1)));
        fails(IllegalArgumentException.class, () -> new Main.Value(-1));
        fails(NullPointerException.class, () -> new Main.Coordinator(null,new Main.Notices()));
        fails(NullPointerException.class, () -> new Main.Coordinator(Arrays.asList((IntPredicate)null),new Main.Notices()));
        fails(NullPointerException.class, () -> new Main.Coordinator(List.of(),null));
        System.out.println("Collaboration worked contracts: "+checks+" passed");
    }
}
