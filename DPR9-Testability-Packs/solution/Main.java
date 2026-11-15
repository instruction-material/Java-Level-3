import journal.*;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode = args.length == 0 ? "core" : args[0];
        JournalAPI journal = switch (mode) {
            case "baseline" -> new LegacyJournal();
            case "core" -> new DispatchJournal();
            default -> throw new IllegalArgumentException("mode");
        };
        journal.record("lamp", 2);
    }
}
