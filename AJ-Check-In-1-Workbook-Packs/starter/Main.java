import java.util.ArrayList;
import java.util.List;

public class Main {
    static int stringLength(String text) {
        // TODO: Count the characters recursively.
        throw new UnsupportedOperationException("Count the characters recursively");
    }

    static int pascal(int row, int column) {
        // TODO: Compute a Pascal entry.
        throw new UnsupportedOperationException("Compute a Pascal entry");
    }

    static boolean linearContains(int[] values, int target) {
        // TODO: Scan for the target.
        throw new UnsupportedOperationException("Scan for the target");
    }

    static int binaryIndex(int[] values, int target) {
        // TODO: Search the sorted array iteratively.
        throw new UnsupportedOperationException("Search the sorted array iteratively");
    }

    static int binaryRange(int[] values, int target, int lower, int upper) {
        // TODO: Search the smaller sorted range recursively.
        throw new UnsupportedOperationException("Search the smaller sorted range recursively");
    }

    static List<Integer> trace(int value) {
        List<Integer> output = new ArrayList<>();
        visit(value, output);
        return output;
    }

    private static void visit(int value, List<Integer> output) {
        output.add(value);
        if (value > 2) {
            visit(value - 1, output);
            visit(value - 2, output);
        }
    }

    static int pascalMemo(int row, int column, int[][] known) {
        // TODO: Cache each computed Pascal entry.
        throw new UnsupportedOperationException("Cache each computed Pascal entry");
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("memo")) {
            System.out.println(pascalMemo(10, 5, new int[11][11]));
            return;
        }
        if (args.length == 1 && args[0].equals("trace")) {
            System.out.println(trace(5));
            return;
        }
        int[] ordered = {2, 4, 8, 16};
        System.out.println(stringLength("hello world!"));
        System.out.println(pascal(4, 2));
        System.out.println(linearContains(new int[]{9, 2, 8}, 2));
        System.out.println(binaryIndex(ordered, 16));
        System.out.println(binaryRange(ordered, 7, 0, ordered.length - 1));
    }
}
