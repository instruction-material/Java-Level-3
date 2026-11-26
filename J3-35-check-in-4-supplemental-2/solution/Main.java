import java.util.HashMap;
import java.util.List;

public class Main {
    record Entry(int key, int value) {}

    static boolean valid(List<Entry> before, List<List<Entry>> after) {
        if (after.isEmpty()) return false;
        var remaining = new HashMap<Integer, Integer>();
        for (Entry entry : before) {
            if (remaining.putIfAbsent(entry.key(), entry.value()) != null) return false;
        }
        for (int slot = 0; slot < after.size(); slot++) {
            for (Entry entry : after.get(slot)) {
                if (Math.floorMod(entry.key(), after.size()) != slot
                        || !remaining.remove(entry.key(), entry.value())) return false;
            }
        }
        return remaining.isEmpty();
    }

    public static void main(String[] args) {
        var a = new Entry(2, 21);
        var b = new Entry(-1, 34);
        var expected = List.of(a, b);
        System.out.println(valid(expected, List.of(List.of(), List.of(), List.of(a, b))));
        System.out.println(valid(expected, List.of(List.of(a), List.of(), List.of(b))));
    }
}
