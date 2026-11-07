package notes.ports;
import notes.domain.Note;
@FunctionalInterface
public interface NoteSink { void saved(Note note); }
