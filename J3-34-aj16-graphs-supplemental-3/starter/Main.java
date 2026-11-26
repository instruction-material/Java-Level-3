import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    record Arc(String from, String to) {}
    record Degree(int incoming, int outgoing) {}

    static Map<String, Degree> degrees(List<String> initial, List<Arc> arcs) {
        // TODO: Count the unique incoming and outgoing arcs of each vertex.
        throw new UnsupportedOperationException("Count the unique incoming and outgoing arcs of each vertex");
    }

    public static void main(String[] args) {
        var result = degrees(List.of("A", "B", "C", "D"),
                List.of(new Arc("A", "B"), new Arc("A", "B"), new Arc("B", "A"), new Arc("B", "B")));
        for (var pair : result.entrySet()) {
            System.out.println(pair.getKey() + ": " + pair.getValue().incoming() + " " + pair.getValue().outgoing());
        }
    }
}
