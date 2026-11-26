import java.util.*;
import java.nio.file.*;
import java.io.*;

class Main {
    enum Status { READY, WAITING }
    record Row(String id, int count, Status status) {}
    record Rejection(int line, String reason) {}
    record Import(List<Row> accepted, List<Rejection> rejected) {}

    static Row parse(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length != 3 || fields[0].isBlank()) throw new IllegalArgumentException("expected three fields and an id");
        int count;
        try { count = Integer.parseInt(fields[1].strip()); }
        catch (NumberFormatException error) { throw new IllegalArgumentException("invalid count"); }
        if (count < 0) throw new IllegalArgumentException("negative count");
        Status status;
        try { status = Status.valueOf(fields[2].strip()); }
        catch (IllegalArgumentException error) { throw new IllegalArgumentException("invalid status"); }
        return new Row(fields[0].strip(), count, status);
    }

    static Import importRows(List<Row> existing, Path file) throws IOException {
        var accepted = new LinkedHashMap<String, Row>();
        for (Row row : existing) {
            if (accepted.putIfAbsent(row.id(), row) != null) {
                throw new IllegalArgumentException("duplicate existing id");
            }
        }
        var rejected = new ArrayList<Rejection>();
        try (var reader = Files.newBufferedReader(file)) {
            int number = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                number++;
                try {
                    Row row = parse(line);
                    if (accepted.putIfAbsent(row.id(), row) != null) {
                        throw new IllegalArgumentException("duplicate id");
                    }
                } catch (IllegalArgumentException invalid) {
                    rejected.add(new Rejection(number, invalid.getMessage()));
                }
            }
        }
        return new Import(List.copyOf(accepted.values()), List.copyOf(rejected));
    }

    public static void main(String[] args) throws IOException {
        var result = importRows(List.of(new Row("existing", 1, Status.READY)), Path.of("records.csv"));
        System.out.println(result.accepted().stream().map(Row::id).toList());
        System.out.println(result.rejected());
    }
}
