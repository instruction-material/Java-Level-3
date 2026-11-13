package lab;

public final class BookingData {
    private BookingData() {}
    public enum Machine {
        STANDARD(0, 120), LASER(2, 60), CNC(3, 45);
        private final int training;
        private final int limit;
        Machine(int training, int limit) { this.training = training; this.limit = limit; }
        public int requiredTraining() { return training; }
        public int limitMinutes() { return limit; }
    }
    public record Member(boolean suspended, boolean revoked, int trainingLevel) {}
    public record Request(Machine machine, Member member, int minutes,
                          boolean closed, boolean supervised, boolean busy) {}
    public record Decision(boolean allowed, String reason, int grantedMinutes) {}
    public record Outcome(Decision decision, int credits) {}
}
