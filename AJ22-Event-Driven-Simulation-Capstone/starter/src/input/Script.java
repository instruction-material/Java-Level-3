package input;
import model.Event;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class Script {
    public record Input(Map<String,Integer> inventory, List<Event> events) {}

    public static Input load(Path file) throws IOException {
        var inventory = new LinkedHashMap<String,Integer>();
        var events = new ArrayList<Event>();
        int number = 0;
        try (var reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                number++;
                String content = line.strip();
                if (content.isEmpty() || content.startsWith("#")) continue;
                try {
                    String[] parts = content.split("\\s+");
                    if (parts.length != 3) throw new IllegalArgumentException("Expected three fields");
                    if (parts[0].equals("stock")) {
                        int copies = Integer.parseInt(parts[2]);
                        if (!events.isEmpty() || copies < 1 || inventory.putIfAbsent(parts[1], copies) != null) {
                            throw new IllegalArgumentException("Expected new positive stock before events");
                        }
                    } else {
                        events.add(event(parts[0], parts[1], parts[2]));
                    }
                } catch (IllegalArgumentException invalid) {
                    throw new IllegalArgumentException("Line " + number + ": " + invalid.getMessage(), invalid);
                }
            }
        }
        return new Input(Map.copyOf(inventory), List.copyOf(events));
    }

    private static Event event(String verb, String member, String title) {
        return switch (verb) {
            case "request" -> new Event.Request(member, title);
            case "return" -> new Event.Return(member, title);
            case "cancel" -> new Event.Cancel(member, title);
            default -> throw new IllegalArgumentException("Unknown event " + verb);
        };
    }
}
