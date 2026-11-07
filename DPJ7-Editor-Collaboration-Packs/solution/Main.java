import editor.*;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        EventBus events = new EventBus();
        events.subscribe(event -> System.out.println(event.kind().name().toLowerCase(Locale.ROOT)
            + ":" + event.before() + "->" + event.after()));
        EditorMediator editor = new EditorMediator("Ready",
            new Validation(List.of(Validation.nonBlank(), Validation.maxLength(12))), events);
        System.out.println(editor.replace("Checked"));
        System.out.println(editor.replace(" "));
        System.out.println(editor.undo());
        System.out.println(editor.text() + ":" + editor.undoDepth());
        Nodes.Node tree = new Nodes.Group(List.of(new Nodes.Text("a"),
            new Nodes.Group(List.of(new Nodes.Text("b"), new Nodes.Text("c")))));
        System.out.println(tree.accept(new TextVisitor()));
        System.out.println(tree.accept(new Nodes.LeafCount()));
    }
}
