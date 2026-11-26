import java.util.ArrayList;
import java.util.List;

public class Main {
    static int stringLength(String text) {
        if (text.isEmpty()) return 0;
        return 1 + stringLength(text.substring(1));
    }

    static int pascal(int row, int column) {
        if (column < 0 || column > row) return 0;
        if (row == 0) return 1;
        return pascal(row - 1, column - 1) + pascal(row - 1, column);
    }

    static boolean linearContains(int[] values, int target) {
        for (int value : values) {
            if (value == target) return true;
        }
        return false;
    }

    static int binaryIndex(int[] values, int target) {
        int lower = 0;
        int upper = values.length - 1;
        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            if (values[middle] == target) return middle;
            if (values[middle] < target) lower = middle + 1;
            else upper = middle - 1;
        }
        return -1;
    }

    static int binaryRange(int[] values, int target, int lower, int upper) {
        if (lower > upper) return -1;
        int middle = lower + (upper - lower) / 2;
        if (values[middle] == target) return middle;
        if (values[middle] < target) {
            return binaryRange(values, target, middle + 1, upper);
        }
        return binaryRange(values, target, lower, middle - 1);
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
        if (column < 0 || column > row) return 0;
        if (row == 0) return 1;
        if (known[row][column] == 0) {
            known[row][column] = pascalMemo(row - 1, column - 1, known)
                    + pascalMemo(row - 1, column, known);
        }
        return known[row][column];
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
