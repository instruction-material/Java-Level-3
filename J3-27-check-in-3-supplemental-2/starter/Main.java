import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    record Dispatch(String id, int count) {}

    static List<Dispatch> batch(List<String> arrivals) {
        // TODO: Count IDs and dispatch in first arrival order.
        throw new UnsupportedOperationException("Count IDs and dispatch in first arrival order");
    }

    public static void main(String[] args) {
        System.out.println(batch(List.of("A2", "B1", "A2", "C3", "B1", "A2")));
    }
}
