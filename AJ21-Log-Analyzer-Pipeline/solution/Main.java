import java.util.*;
import java.time.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.*;

class Main {
    record Event(LocalTime time, String type, String actor) {}
    record Input(List<Event> events, List<Integer> rejected) {}

    static Event parse(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 3 || fields[1].isBlank() || fields[2].isBlank()) {
            throw new IllegalArgumentException("Expected time, type and actor");
        }
        return new Event(LocalTime.parse(fields[0].strip()), fields[1].strip(), fields[2].strip());
    }

    static Input read(Path path) throws IOException {
        var events = new ArrayList<Event>();
        var rejected = new ArrayList<Integer>();
        try (var reader = Files.newBufferedReader(path)) {
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try { events.add(parse(line)); }
                catch (IllegalArgumentException | DateTimeException invalid) { rejected.add(lineNumber); }
            }
        }
        return new Input(List.copyOf(events), List.copyOf(rejected));
    }

    static TreeMap<String, Long> countLoop(List<Event> events) {
        var counts = new TreeMap<String, Long>();
        for (Event event : events) counts.merge(event.type(), 1L, Long::sum);
        return counts;
    }

    static TreeMap<String, Long> countStream(List<Event> events) {
        return events.stream().collect(Collectors.groupingBy(Event::type, TreeMap::new, Collectors.counting()));
    }

    static Optional<LocalTime> first(List<Event> events) {
        return events.stream().map(Event::time).min(Comparator.naturalOrder());
    }

    static Optional<LocalTime> last(List<Event> events) {
        return events.stream().map(Event::time).max(Comparator.naturalOrder());
    }

    static Optional<String> mostFrequent(Map<String, Long> counts) {
        return counts.entrySet().stream()
                .min(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey);
    }

    public static void main(String[] args) throws IOException {
        Input input = read(Path.of("events.txt"));
        var counts = countStream(input.events());
        System.out.println(counts);
        System.out.println(first(input.events()).orElseThrow() + " to " + last(input.events()).orElseThrow());
        System.out.println(mostFrequent(counts).orElseThrow());
        System.out.println(input.rejected());
    }
}
