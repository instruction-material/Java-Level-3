package journal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
/** Working candidate: add constructor seams while retaining default client calls. */
public final class DispatchJournal implements JournalAPI {
    public Entry record(String label, int count) {
        Entry.validate(label, count);
        Entry entry = new Entry(Instant.now(), ThreadLocalRandom.current().nextInt(1_000_000), label, count);
        System.out.print(entry.line());
        return entry;
    }
}
