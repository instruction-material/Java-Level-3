import java.util.List;
import notes.application.NoteService;
import notes.infra.InMemoryNotes;
import notes.plugins.PluginCatalog;
import notes.ui.NoteViewModel;
/** Composition root is the only application code that chooses infrastructure. */
public class Main {
    public static void main(String[] args) {
        PluginCatalog plugins=PluginCatalog.discover(Main.class.getClassLoader());
        InMemoryNotes repository=new InMemoryNotes();
        NoteService service=new NoteService(repository,plugins,note -> System.out.println(note.id()+":"+note.text()));
        NoteViewModel view=new NoteViewModel(service);
        System.out.println(plugins.availableIds());
        view.add("a","hi",List.of("upper","bracket"));
        System.out.println(view.state().rows());
        service.replace("a","Ready",List.of("bracket"));
        view.refresh();
        System.out.println(view.state().rows());
        System.out.println(repository.all().size());
    }
}
