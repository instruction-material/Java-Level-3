import java.util.*;
import java.nio.file.*;
import java.io.*;

class Main {
    enum Status { READY, WAITING }
    record Row(String id, int count, Status status) {}
    record Rejection(int line, String reason) {}
    record Import(List<Row> accepted, List<Rejection> rejected) {}

    static Row parse(String line) {
        // TODO: Validate the fields of a single input row.
        throw new UnsupportedOperationException("Validate the fields of a single input row");
    }

    static Import importRows(List<Row> existing, Path file) throws IOException {
        // TODO: Preserve accepted rows and record each rejected line.
        throw new UnsupportedOperationException("Preserve accepted rows and record each rejected line");
    }

    public static void main(String[] args) throws IOException {
        var result = importRows(List.of(new Row("existing", 1, Status.READY)), Path.of("records.csv"));
        System.out.println(result.accepted().stream().map(Row::id).toList());
        System.out.println(result.rejected());
    }
}
