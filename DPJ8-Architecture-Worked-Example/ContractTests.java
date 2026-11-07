import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++;if (!java.util.Objects.equals(actual,expected)) throw new AssertionError(actual+" != "+expected);
    }
    private static void fails(Class<? extends Throwable> type,Runnable work) {
        checks++;
        try { work.run(); } catch (Throwable error) {
            if(type.isInstance(error))return;
            throw new AssertionError(error);
        }
        throw new AssertionError(type.getSimpleName());
    }
    public static void main(String[] args) {
        Main.Memory store=new Main.Memory();
        equal(store.find(1),Optional.empty());equal(store.all(),List.of());
        fails(IllegalArgumentException.class,()->store.find(0));
        fails(NullPointerException.class,()->store.save(null));
        fails(IllegalArgumentException.class,()->new Main.Item(0,"ok"));
        fails(NullPointerException.class,()->new Main.Item(1,null));
        fails(IllegalArgumentException.class,()->new Main.Item(1,"a\nb"));
        fails(IllegalArgumentException.class,()->new Main.Item(1,"a\rb"));
        equal(new Main.Item(1,"").text(),"");
        List<Main.Item> notices=new ArrayList<>();
        Main.Service service=new Main.Service(store,x->x+"!",item->{
            equal(store.find(item.id()),Optional.of(item));notices.add(item);
        });
        fails(NullPointerException.class,()->new Main.Service(null,x->x,n->{}));
        fails(NullPointerException.class,()->new Main.Service(store,null,n->{}));
        fails(NullPointerException.class,()->new Main.Service(store,x->x,null));
        equal(service.write(2,"hi"),new Main.Item(2,"hi!"));
        List<Main.Item> snapshot=service.list();
        fails(UnsupportedOperationException.class,()->snapshot.clear());
        service.write(1,"ready");
        service.write(2,"new");
        equal(snapshot,List.of(new Main.Item(2,"hi!")));
        equal(service.list(),List.of(new Main.Item(2,"new!"),new Main.Item(1,"ready!")));
        equal(notices.size(),3);
        Main.ViewModel view=new Main.ViewModel(service);
        equal(view.state().rows(),List.of());view.refresh();
        Main.Screen previous=view.state();
        equal(previous.rows(),List.of("2=new!","1=ready!"));
        fails(UnsupportedOperationException.class,()->previous.rows().clear());
        service.write(3,"third");equal(view.state(),previous);
        view.refresh();equal(view.state().rows(),List.of("2=new!","1=ready!","3=third!"));
        equal(previous.rows(),List.of("2=new!","1=ready!"));
        fails(NullPointerException.class,()->new Main.ViewModel(null));
        fails(NullPointerException.class,()->new Main.Screen(null));
        List<String> rows=new ArrayList<>(List.of("x"));Main.Screen copied=new Main.Screen(rows);
        rows.clear();equal(copied.rows(),List.of("x"));
        Main.Service badOutput=new Main.Service(store,x->null,n->{});
        fails(NullPointerException.class,()->badOutput.write(4,"ok"));equal(store.find(4),Optional.empty());
        Main.Service badSink=new Main.Service(store,x->x,n->{throw new IllegalStateException("notification");});
        fails(IllegalStateException.class,()->badSink.write(4,"committed"));
        equal(store.find(4),Optional.of(new Main.Item(4,"committed")));
        fails(IllegalStateException.class,()->badSink.write(4,"second"));
        equal(store.find(4),Optional.of(new Main.Item(4,"second")));
        Main.Service[] owner=new Main.Service[1];
        owner[0]=new Main.Service(store,x->x,n->{
            fails(IllegalStateException.class,()->owner[0].write(5,"nested"));
            equal(owner[0].list(),store.all());
        });
        owner[0].write(4,"guarded");equal(store.find(5),Optional.empty());
        System.out.println("Architecture worked contracts: "+checks+" passed");
    }
}
