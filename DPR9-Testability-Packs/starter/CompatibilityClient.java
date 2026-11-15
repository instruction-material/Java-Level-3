import journal.*;
/** Existing construction and calls compile unchanged; no framework is introduced. */
public final class CompatibilityClient {
    private CompatibilityClient() {}
    public static Entry record(JournalAPI journal) { return journal.record("lamp", 2); }
    public static Entry defaultClient() { return new DispatchJournal().record("lamp", 2); }
}
