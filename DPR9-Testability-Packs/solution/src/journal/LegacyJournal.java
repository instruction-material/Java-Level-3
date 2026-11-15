package journal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
/** Frozen working before design: hard-coded time, random source and global output. */
public final class LegacyJournal implements JournalAPI {
    public Entry record(String label, int count) {
        Entry.validate(label, count);
        Entry entry = new Entry(Instant.now(), ThreadLocalRandom.current().nextInt(1_000_000), label, count);
        System.out.print(entry.line());
        return entry;
    }
}
