import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    static Map<String, Integer> replay(List<String> actions) {
        // TODO: Undo the latest addition and update the item counts.
        throw new UnsupportedOperationException("Undo the latest addition and update the item counts");
    }

    public static void main(String[] args) {
        System.out.println(replay(List.of("UNDO", "lamp", "bulb", "lamp", "UNDO", "fan")));
    }
}
