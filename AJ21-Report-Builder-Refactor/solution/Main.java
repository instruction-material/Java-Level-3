import java.util.*;

class Main {
    record Row(String name, int quantity) {}
    record Totals(Map<String, Long> byName, int skipped) {}

    static Optional<Row> parse(String line) {
        String[] parts = line.split(":", -1);
        if (parts.length != 2 || parts[0].isBlank()) return Optional.empty();
        try {
            int quantity = Integer.parseInt(parts[1].strip());
            return quantity < 0 ? Optional.empty() : Optional.of(new Row(parts[0].strip(), quantity));
        } catch (NumberFormatException invalid) {
            return Optional.empty();
        }
    }

    static Totals aggregate(List<String> lines) {
        var counts = new TreeMap<String, Long>();
        int skipped = 0;
        for (String line : lines) {
            var row = parse(line);
            if (row.isPresent()) {
                Row value = row.orElseThrow();
                counts.merge(value.name(), (long) value.quantity(), Long::sum);
            } else {
                skipped++;
            }
        }
        return new Totals(Map.copyOf(counts), skipped);
    }

    static List<String> format(Totals totals) {
        var rows = new ArrayList<String>();
        new TreeMap<>(totals.byName()).forEach((name, count) -> rows.add(name + "=" + count));
        rows.add("skipped=" + totals.skipped());
        return List.copyOf(rows);
    }

    static List<String> report(List<String> lines) {
        return format(aggregate(lines));
    }

    static List<String> sample() {
        return List.of("oak:2", "bad", "elm:4", "oak:3");
    }

    public static void main(String[] args) {
        report(sample()).forEach(System.out::println);
    }
}
