package journal;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
public final class DispatchJournal implements JournalAPI {
    private final Clock clock;
    private final IntSupplier ids;
    private final EntrySink sink;
    public DispatchJournal() {
        this(Clock.systemUTC(), () -> ThreadLocalRandom.current().nextInt(1_000_000),
                entry -> System.out.print(entry.line()));
    }
    public DispatchJournal(Clock clock, IntSupplier ids, EntrySink sink) {
        this.clock = Objects.requireNonNull(clock, "clock");
        this.ids = Objects.requireNonNull(ids, "ids");
        this.sink = Objects.requireNonNull(sink, "sink");
    }
    public Entry record(String label, int count) {
        Entry.validate(label, count);
        Entry entry = new Entry(clock.instant(), ids.getAsInt(), label, count);
        try { sink.write(entry); }
        catch (IOException error) { throw new UncheckedIOException("journal", error); }
        return entry;
    }
}
