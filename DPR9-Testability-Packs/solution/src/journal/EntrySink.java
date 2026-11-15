package journal;
import java.io.IOException;
/** An output boundary. The core calls this once per valid record. */
@FunctionalInterface
public interface EntrySink { void write(Entry entry) throws IOException; }
