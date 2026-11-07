import java.util.Objects;
public class Main {
    public record Message(String text) {
        public Message { Objects.requireNonNull(text, "text"); }
    }
    @FunctionalInterface
    public interface Sink { void write(String text); }
    public static final class Sender {
        private final Sink sink;
        public Sender(Sink sink) { this.sink = Objects.requireNonNull(sink, "sink"); }
        public void send(Message message) {
            sink.write(Objects.requireNonNull(message, "message").text());
        }
    }
    public static String demonstration(String text) {
        StringBuilder recorded = new StringBuilder();
        new Sender(recorded::append).send(new Message(text));
        return recorded.toString();
    }
    public static void main(String[] args) {
        System.out.println(demonstration("Ready"));
    }
}
