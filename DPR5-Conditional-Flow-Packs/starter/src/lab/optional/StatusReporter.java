package lab.optional;
import lab.BookingData.Decision;

/** Optional notification collaborator. The required AuditTrail stays required. */
public interface StatusReporter {
    void started();
    void completed(Decision decision, int credits);
    void failed(RuntimeException error);
    static StatusReporter none() { return None.INSTANCE; }
    enum None implements StatusReporter {
        INSTANCE;
        @Override public void started() {}
        @Override public void completed(Decision decision, int credits) {}
        @Override public void failed(RuntimeException error) {}
    }
}
