package preview;
import api.*;
import java.util.*;
public final class RenamePreview {
    private RenamePreview() {}
    public static SessionView rename(SessionView source, String oldID, String newID) {
        Objects.requireNonNull(source, "snapshot");
        Rules.id(oldID, "oldID"); Rules.id(newID, "newID");
        if (!source.open()) throw new IllegalStateException("closed");
        boolean active = source.enrolled().contains(oldID);
        boolean queued = source.waiting().contains(oldID);
        if ((!active && !queued) || oldID.equals(newID)) return source;
        if (source.enrolled().contains(newID) || source.waiting().contains(newID))
            throw new IllegalArgumentException("newID");
        List<String> enrolled = new ArrayList<>(source.enrolled());
        List<String> waiting = new ArrayList<>(source.waiting());
        List<String> history = new ArrayList<>(source.history());
        List<String> selected = active ? enrolled : waiting;
        selected.set(selected.indexOf(oldID), newID);
        history.add("rename:"+oldID+":"+newID);
        return new SessionView(source.id(), source.capacity(), source.open(), enrolled, waiting, history);
    }
}
