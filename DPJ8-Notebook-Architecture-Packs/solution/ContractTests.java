import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.UnaryOperator;
import notes.application.NoteService;
import notes.domain.Note;
import notes.infra.InMemoryNotes;
import notes.plugins.PluginCatalog;
import notes.plugins.TextPlugin;
import notes.ports.NoteRepository;
import notes.ui.NoteViewModel;

public class ContractTests {
    private static int checks;
    private static void equal(Object actual,Object expected) {
        checks++;
        if (!java.util.Objects.equals(actual,expected)) throw new AssertionError(actual+" != "+expected);
    }
    private static void fails(Class<? extends Throwable> expected,Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable error) {
            if (expected.isInstance(error)) return;
            throw new AssertionError("Expected "+expected.getSimpleName()+", got "+error,error);
        }
        throw new AssertionError("Expected "+expected.getSimpleName());
    }
    private static TextPlugin plugin(String id,UnaryOperator<String> operation) {
        return new TextPlugin() {
            public String id() { return id; }
            public String apply(String text) { return operation.apply(text); }
        };
    }
    private static PluginCatalog emptyPlugins() { return new PluginCatalog(List.of()); }
    private static void repository() {
        InMemoryNotes store=new InMemoryNotes();
        equal(store.find("a"),Optional.empty()); equal(store.all(),List.of());
        fails(NullPointerException.class,() -> store.find(null));
        fails(IllegalArgumentException.class,() -> store.find("Bad"));
        fails(NullPointerException.class,() -> store.save(null));
        Note first=new Note("b","first");
        store.save(first);store.save(new Note("a","second"));
        List<Note> snapshot=store.all();
        equal(store.find("b"),Optional.of(first));
        equal(snapshot,List.of(first,new Note("a","second")));
        fails(UnsupportedOperationException.class,() -> snapshot.add(new Note("c","third")));
        fails(UnsupportedOperationException.class,() -> snapshot.clear());
        store.save(new Note("b","changed"));
        equal(store.all(),List.of(new Note("b","changed"),new Note("a","second")));
        equal(snapshot,List.of(first,new Note("a","second")));
        store.save(new Note("c","")); equal(store.all().size(),3);
        for (String invalid : List.of("","Bad","bad id","a\nb","1a"))
            fails(IllegalArgumentException.class,() -> new Note(invalid,"ok"));
        fails(NullPointerException.class,() -> new Note(null,"ok"));
        fails(NullPointerException.class,() -> new Note("a",null));
        fails(IllegalArgumentException.class,() -> new Note("a","a\nb"));
        fails(IllegalArgumentException.class,() -> new Note("a","a\rb"));
        equal(new Note("a",""),new Note("a",""));
    }
    private static void plugins() {
        List<TextPlugin> supplied=new ArrayList<>();
        supplied.add(plugin("suffix",value -> value+"!"));
        supplied.add(plugin("bracket",value -> "["+value+"]"));
        PluginCatalog catalog=new PluginCatalog(supplied); supplied.clear();
        equal(catalog.availableIds(),List.of("bracket","suffix"));
        fails(UnsupportedOperationException.class,() -> catalog.availableIds().clear());
        equal(catalog.apply("hi",List.of()),"hi");
        equal(catalog.apply("hi",List.of("suffix","bracket")),"[hi!]");
        equal(catalog.apply("hi",List.of("bracket","suffix")),"[hi]!");
        equal(catalog.apply("hi",List.of("suffix","suffix")),"hi!!");
        equal(catalog.apply("",List.of("bracket")),"[]");
        fails(NullPointerException.class,() -> catalog.apply(null,List.of()));
        fails(IllegalArgumentException.class,() -> catalog.apply("a\nb",List.of()));
        fails(NullPointerException.class,() -> catalog.apply("ok",null));
        fails(NullPointerException.class,() -> catalog.apply("ok",Arrays.asList("suffix",null)));
        fails(IllegalArgumentException.class,() -> catalog.apply("ok",List.of("Bad")));
        fails(IllegalArgumentException.class,() -> catalog.apply("ok",List.of("missing")));
        fails(NullPointerException.class,() -> new PluginCatalog(null));
        fails(NullPointerException.class,() -> new PluginCatalog(Arrays.asList((TextPlugin)null)));
        fails(IllegalArgumentException.class,() -> new PluginCatalog(List.of(plugin("same",x->x),plugin("same",x->x))));
        fails(IllegalArgumentException.class,() -> new PluginCatalog(List.of(plugin("Bad",x->x))));
        fails(NullPointerException.class,() -> new PluginCatalog(List.of(plugin(null,x->x))));
        int[] calls={0};
        PluginCatalog counted=new PluginCatalog(List.of(plugin("count",x->{calls[0]++;return x;})));
        fails(IllegalArgumentException.class,() -> counted.apply("ok",List.of("count","missing")));
        equal(calls[0],0);
        fails(IllegalStateException.class,() -> new PluginCatalog(List.of(plugin("fail",x->{throw new IllegalStateException("failed");}))).apply("ok",List.of("fail")));
        fails(NullPointerException.class,() -> new PluginCatalog(List.of(plugin("null",x->null))).apply("ok",List.of("null")));
        fails(IllegalArgumentException.class,() -> new PluginCatalog(List.of(plugin("newline",x->"a\nb"))).apply("ok",List.of("newline")));
        List<String> mutable=new ArrayList<>(List.of("clear","suffix"));
        PluginCatalog captured=new PluginCatalog(List.of(plugin("clear",x->{mutable.clear();return x;}),plugin("suffix",x->x+"!")));
        equal(captured.apply("ok",mutable),"ok!");
        PluginCatalog discovered=PluginCatalog.discover(ContractTests.class.getClassLoader());
        equal(discovered.availableIds(),List.of("bracket","upper"));
        equal(discovered.apply("hi",List.of("upper","bracket")),"[HI]");
        fails(NullPointerException.class,() -> PluginCatalog.discover(null));
    }
    private static void service() {
        InMemoryNotes store=new InMemoryNotes();
        List<Note> notices=new ArrayList<>();
        PluginCatalog plugins=new PluginCatalog(List.of(plugin("bang",x->x+"!")));
        NoteService service=new NoteService(store,plugins,note->{
            equal(store.find(note.id()),Optional.of(note)); notices.add(note);
        });
        fails(NullPointerException.class,() -> new NoteService(null,plugins,n->{}));
        fails(NullPointerException.class,() -> new NoteService(store,null,n->{}));
        fails(NullPointerException.class,() -> new NoteService(store,plugins,null));
        equal(service.list(),List.of());
        equal(service.create("a","hi",List.of("bang")),new Note("a","hi!"));
        equal(notices,List.of(new Note("a","hi!")));
        fails(IllegalArgumentException.class,() -> service.create("a","again",List.of()));
        equal(store.all(),List.of(new Note("a","hi!")));
        fails(NoSuchElementException.class,() -> service.replace("missing","ok",List.of()));
        equal(notices.size(),1);
        fails(IllegalArgumentException.class,() -> service.replace("a","ok",List.of("missing")));
        equal(store.find("a"),Optional.of(new Note("a","hi!")));
        fails(NullPointerException.class,() -> service.replace("a",null,List.of()));
        fails(IllegalArgumentException.class,() -> service.create("b","a\nb",List.of()));
        equal(store.find("b"),Optional.empty());
        equal(service.replace("a","new",List.of("bang")),new Note("a","new!"));
        equal(notices.size(),2);
        equal(service.replace("a","new!",List.of()),new Note("a","new!"));
        equal(notices.size(),3);
        List<Note> snapshot=service.list();
        fails(UnsupportedOperationException.class,() -> snapshot.clear());
        service.create("b","",List.of()); equal(snapshot,List.of(new Note("a","new!")));
        InMemoryNotes committed=new InMemoryNotes();
        NoteService failingSink=new NoteService(committed,emptyPlugins(),n->{throw new IllegalStateException("sink failed");});
        fails(IllegalStateException.class,() -> failingSink.create("a","one",List.of()));
        equal(committed.find("a"),Optional.of(new Note("a","one")));
        fails(IllegalStateException.class,() -> failingSink.replace("a","two",List.of()));
        equal(committed.find("a"),Optional.of(new Note("a","two")));
        equal(committed.all().size(),1);
        NoteService[] owner=new NoteService[1];
        InMemoryNotes nestedStore=new InMemoryNotes();
        owner[0]=new NoteService(nestedStore,emptyPlugins(),n->{
            equal(owner[0].list(),List.of(n));
            fails(IllegalStateException.class,() -> owner[0].create("nested","bad",List.of()));
            fails(IllegalStateException.class,() -> owner[0].replace("a","bad",List.of()));
        });
        owner[0].create("a","one",List.of()); equal(nestedStore.all(),List.of(new Note("a","one")));
        owner[0].replace("a","two",List.of()); equal(nestedStore.all(),List.of(new Note("a","two")));
        NoteRepository other=new NoteRepository() {
            private Note saved;
            public Optional<Note> find(String id) { return Optional.ofNullable(saved); }
            public List<Note> all() { return saved==null?List.of():List.of(saved); }
            public void save(Note note) { saved=note; }
        };
        NoteService injected=new NoteService(other,emptyPlugins(),n->{});
        injected.create("port","injected",List.of()); equal(injected.list(),List.of(new Note("port","injected")));
    }
    private static void view() {
        InMemoryNotes store=new InMemoryNotes();
        NoteService service=new NoteService(store,emptyPlugins(),n->{});
        NoteViewModel model=new NoteViewModel(service);
        equal(model.state().rows(),List.of());
        service.create("b","two",List.of()); equal(model.state().rows(),List.of());
        model.refresh(); NoteViewModel.State previous=model.state();
        equal(previous.rows(),List.of("b=two"));
        fails(UnsupportedOperationException.class,() -> previous.rows().clear());
        model.add("a","one",List.of()); equal(model.state().rows(),List.of("b=two","a=one"));
        equal(previous.rows(),List.of("b=two"));
        service.replace("b","new",List.of()); equal(model.state().rows(),List.of("b=two","a=one"));
        model.refresh(); equal(model.state().rows(),List.of("b=new","a=one"));
        fails(NullPointerException.class,() -> new NoteViewModel(null));
        fails(NullPointerException.class,() -> new NoteViewModel.State(null));
        List<String> rows=new ArrayList<>(List.of("x"));NoteViewModel.State copy=new NoteViewModel.State(rows);
        rows.clear();equal(copy.rows(),List.of("x"));
        NoteViewModel failingView=new NoteViewModel(new NoteService(store,emptyPlugins(),n->{throw new IllegalStateException("sink");}));
        fails(IllegalStateException.class,() -> failingView.add("c","three",List.of()));
        equal(failingView.state().rows(),List.of());
        failingView.refresh();equal(failingView.state().rows(),List.of("b=new","a=one","c=three"));
    }
    private static void optional() {
        InMemoryNotes store=new InMemoryNotes();
        List<Note> notices=new ArrayList<>();
        NoteService service=new NoteService(store,new PluginCatalog(List.of(plugin("bang",x->x+"!"))),notices::add);
        service.create("a","one",List.of());
        equal(service.copy("a","b",List.of("bang")),new Note("b","one!"));
        equal(store.find("a"),Optional.of(new Note("a","one")));
        equal(notices,List.of(new Note("a","one"),new Note("b","one!")));
        fails(IllegalArgumentException.class,() -> service.copy("a","b",List.of()));
        fails(NoSuchElementException.class,() -> service.copy("missing","c",List.of()));
        fails(IllegalArgumentException.class,() -> service.copy("a","c",List.of("missing")));
        equal(store.find("c"),Optional.empty()); equal(notices.size(),2);
        NoteService failing=new NoteService(store,emptyPlugins(),n->{throw new IllegalStateException("sink");});
        fails(IllegalStateException.class,() -> failing.copy("a","c",List.of()));
        equal(store.find("c"),Optional.of(new Note("c","one")));
    }
    public static void main(String[] args) {
        if (args.length>1 || (args.length==1 && !args[0].equals("core"))) throw new IllegalArgumentException("use core or no arguments");
        repository();plugins();service();view();
        String label="Core";
        if (args.length==0) { optional(); label="Complete"; }
        System.out.println(label+" architecture contracts: "+checks+" passed");
    }
}
