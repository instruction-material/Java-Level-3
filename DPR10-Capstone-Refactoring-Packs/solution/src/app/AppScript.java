package app;
import api.*;
import java.util.List;
import java.util.Objects;
/** Frozen command boundary: parse, dispatch, report a failure and continue. */
public final class AppScript {
    private AppScript() {}
    public static String run(DeskAPI desk, List<String> lines) {
        Objects.requireNonNull(desk, "desk");
        List<String> input = List.copyOf(Objects.requireNonNull(lines, "lines"));
        StringBuilder text = new StringBuilder();
        for (String line : input) {
            String clean = line.trim();
            if (clean.isEmpty()) continue;
            String[] words = clean.split("\\s+");
            try {
                switch (words[0]) {
                    case "open" -> {
                        arity(words, 3);
                        desk.open(words[1], number(words[2]));
                        text.append("opened\n");
                    }
                    case "join" -> {
                        arity(words, 3);
                        text.append(desk.join(words[1], words[2])).append('\n');
                    }
                    case "leave" -> {
                        arity(words, 3);
                        text.append(desk.leave(words[1], words[2])).append('\n');
                    }
                    case "close" -> {
                        arity(words, 2);
                        text.append(desk.close(words[1])).append('\n');
                    }
                    case "view" -> {
                        arity(words, 2);
                        SessionView value = desk.view(words[1]);
                        text.append(value.id()).append('|').append(value.capacity()).append('|')
                            .append(value.open()).append('|').append(value.enrolled()).append('|')
                            .append(value.waiting()).append('|').append(value.history()).append('\n');
                    }
                    case "report" -> {
                        arity(words, 2);
                        text.append(desk.report(words[1]));
                    }
                    default -> throw new IllegalArgumentException("command");
                }
            } catch (RuntimeException failure) {
                text.append("error:").append(failure.getClass().getSimpleName())
                    .append(':').append(failure.getMessage()).append('\n');
            }
        }
        return text.toString();
    }
    private static void arity(String[] words, int wanted) {
        if (words.length != wanted) throw new IllegalArgumentException("command");
    }
    private static int number(String value) {
        try { return Integer.parseInt(value); }
        catch (NumberFormatException failure) { throw new IllegalArgumentException("capacity", failure); }
    }
}
