import java.util.*;

class Main {
    record Row(String name, int quantity) {}
    record Totals(Map<String, Long> byName, int skipped) {}

    static Optional<Row> parse(String line) {
        // TODO: Parse an isolated report row.
        throw new UnsupportedOperationException("Parse an isolated report row");
    }

    static Totals aggregate(List<String> lines) {
        // TODO: Aggregate accepted rows and count rejections.
        throw new UnsupportedOperationException("Aggregate accepted rows and count rejections");
    }

    static List<String> format(Totals totals) {
        // TODO: Format a deterministic report without parsing.
        throw new UnsupportedOperationException("Format a deterministic report without parsing");
    }

    static List<String> report(List<String> lines) {
        // This starter version supplies output for the behavior-preserving refactor.
        var totals = new TreeMap<String, Long>();
        int skipped = 0;
        for (String line : lines) {
            String[] fields = line.split(":", -1);
            int quantity = -1;
            if (fields.length == 2 && !fields[0].isBlank()) {
                try { quantity = Integer.parseInt(fields[1].strip()); }
                catch (NumberFormatException invalid) { quantity = -1; }
            }
            if (quantity < 0) skipped++;
            else totals.merge(fields[0].strip(), (long) quantity, Long::sum);
        }
        var report = new ArrayList<String>();
        totals.forEach((name, count) -> report.add(name + "=" + count));
        report.add("skipped=" + skipped);
        return List.copyOf(report);
    }

    static List<String> sample() {
        return List.of("oak:2", "bad", "elm:4", "oak:3");
    }

    public static void main(String[] args) {
        report(sample()).forEach(System.out::println);
    }
}
