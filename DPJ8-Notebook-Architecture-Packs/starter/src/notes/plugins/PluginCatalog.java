package notes.plugins;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import notes.domain.Note;
public final class PluginCatalog {
    private final Map<String,TextPlugin> plugins;
    public PluginCatalog(Iterable<TextPlugin> providers) {
        Objects.requireNonNull(providers,"providers");
        Map<String,TextPlugin> collected=new LinkedHashMap<>();
        for (TextPlugin provider : providers) {
            Objects.requireNonNull(provider,"provider");
            String id=Note.key(provider.id());
            if (collected.putIfAbsent(id,provider)!=null) throw new IllegalArgumentException("duplicate plugin id");
        }
        plugins=Map.copyOf(collected);
    }
    public static PluginCatalog discover(ClassLoader loader) {
        throw new UnsupportedOperationException("Implement ServiceLoader discovery.");
    }
    public List<String> availableIds() { return plugins.keySet().stream().sorted().toList(); }
    public String apply(String text,List<String> requested) {
        throw new UnsupportedOperationException("Implement a validated plugin pipeline.");
    }
}
