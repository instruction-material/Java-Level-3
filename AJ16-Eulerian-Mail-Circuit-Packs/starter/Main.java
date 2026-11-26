import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    record Street(int first, int second) {}
    enum RouteKind { CIRCUIT, TRAIL, NEITHER }

    static RouteKind classify(int vertices, List<Street> streets) {
        // TODO: Check street connectivity and odd degrees.
        throw new UnsupportedOperationException("Check street connectivity and odd degrees");
    }

    public static void main(String[] args) {
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(1, 2), new Street(2, 0))));
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(1, 2))));
        System.out.println(classify(4, List.of(new Street(0, 1), new Street(2, 3))));
    }
}
