package dispatch.optional;
import dispatch.Quote;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class OptionalChecks {
    private static int checks;
    private OptionalChecks() {}
    private static void equal(Object expected, Object actual) {
        checks++;
        if (!Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("mode");
        Function<LabelQueue, Optional<String>> client = switch (args[0]) {
            case "baseline" -> LegacyQueueClient::printNext;
            case "full" -> QueueClient::printNext;
            default -> throw new IllegalArgumentException("mode");
        };
        for (int count = 0; count <= 20; count++) {
            LabelQueue queue = new LabelQueue();
            for (int i = 0; i < count; i++) queue.add(new Quote("L" + i, i * 10));
            for (int i = 0; i < count; i++) {
                equal(Optional.of("L" + i + ":" + (i * 10)), client.apply(queue));
                equal(count - i - 1, queue.count());
            }
            equal(Optional.empty(), client.apply(queue));
            equal(Optional.empty(), client.apply(queue));
            equal(0, queue.count());
            try { queue.remove(); throw new AssertionError("Missing strict error"); }
            catch (java.util.NoSuchElementException e) { equal("empty queue", e.getMessage()); }
        }
        try { client.apply(null); throw new AssertionError("Missing queue error"); }
        catch (NullPointerException e) { equal("queue", e.getMessage()); }
        LabelQueue queue = new LabelQueue();
        try { queue.add(null); throw new AssertionError("Missing quote error"); }
        catch (NullPointerException e) { equal("quote", e.getMessage()); }
        equal(0, queue.count());
        System.out.println("mode=" + args[0] + " queues=21 checks=" + checks);
    }
}
