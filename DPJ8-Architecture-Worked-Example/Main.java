import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
/** Small explicit-injection example; the assigned pack adds actual ServiceLoader discovery. */
public class Main {
    public record Item(int id,String text) {
        public Item { if (id<=0) throw new IllegalArgumentException("positive id"); text=line(text); }
    }
    public static String line(String value) {
        Objects.requireNonNull(value,"text");
        if (value.indexOf('\r')>=0 || value.indexOf('\n')>=0) throw new IllegalArgumentException("single-line text");
        return value;
    }
    public interface Store {
        Optional<Item> find(int id);
        List<Item> all();
        void save(Item item);
    }
    public static final class Memory implements Store {
        private final Map<Integer,Item> items=new LinkedHashMap<>();
        public Optional<Item> find(int id) {
            if (id<=0) throw new IllegalArgumentException("positive id");
            return Optional.ofNullable(items.get(id));
        }
        public List<Item> all() { return List.copyOf(items.values()); }
        public void save(Item item) { Objects.requireNonNull(item,"item");items.put(item.id(),item); }
    }
    public static final class Service {
        private final Store store;
        private final UnaryOperator<String> processor;
        private final Consumer<Item> notices;
        private boolean busy;
        public Service(Store store,UnaryOperator<String> processor,Consumer<Item> notices) {
            this.store=Objects.requireNonNull(store,"store");
            this.processor=Objects.requireNonNull(processor,"processor");
            this.notices=Objects.requireNonNull(notices,"notices");
        }
        public List<Item> list() { return List.copyOf(store.all()); }
        public Item write(int id,String text) {
            if (busy) throw new IllegalStateException("reentrant mutation");
            busy=true;
            try {
                Item input=new Item(id,text);
                Item output=new Item(id,processor.apply(input.text()));
                store.save(output);notices.accept(output);return output;
            } finally { busy=false; }
        }
    }
    public record Screen(List<String> rows) { public Screen { rows=List.copyOf(rows); } }
    public static final class ViewModel {
        private final Service service;
        private Screen state=new Screen(List.of());
        public ViewModel(Service service) { this.service=Objects.requireNonNull(service,"service"); }
        public Screen state() { return state; }
        public void refresh() { state=new Screen(service.list().stream().map(item->item.id()+"="+item.text()).toList()); }
    }
    public static void main(String[] args) {
        Memory memory=new Memory();
        Service service=new Service(memory,text->text.toUpperCase(java.util.Locale.ROOT),
            item->System.out.println(item.id()+":"+item.text()));
        ViewModel view=new ViewModel(service);
        service.write(1,"hi");view.refresh();System.out.println(view.state().rows());
        service.write(2,"ready");System.out.println(view.state().rows());
        view.refresh();System.out.println(view.state().rows());
    }
}
