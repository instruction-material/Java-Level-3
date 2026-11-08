package notebook.api;

import java.util.List;
import java.util.Optional;

public interface Notebook {
    Note create(String id, String text);
    boolean edit(String id, long expectedVersion, String text);
    Optional<Note> find(String id);
    List<Note> all();
    boolean remove(String id, long expectedVersion);
}
