import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    record Arc(String from, String to) {}
    record Degree(int incoming, int outgoing) {}

    static Map<String, Degree> degrees(List<String> initial, List<Arc> arcs) {
        var totals = new TreeMap<String, Degree>();
        for (String vertex : initial) totals.put(vertex, new Degree(0, 0));
        for (Arc arc : new HashSet<>(arcs)) {
            totals.compute(arc.from(), (key, degree) ->
                    degree == null ? new Degree(0, 1) : new Degree(degree.incoming(), degree.outgoing() + 1));
            totals.compute(arc.to(), (key, degree) ->
                    degree == null ? new Degree(1, 0) : new Degree(degree.incoming() + 1, degree.outgoing()));
        }
        return totals;
    }

    public static void main(String[] args) {
        var result = degrees(List.of("A", "B", "C", "D"),
                List.of(new Arc("A", "B"), new Arc("A", "B"), new Arc("B", "A"), new Arc("B", "B")));
        for (var pair : result.entrySet()) {
            System.out.println(pair.getKey() + ": " + pair.getValue().incoming() + " " + pair.getValue().outgoing());
        }
    }
}
