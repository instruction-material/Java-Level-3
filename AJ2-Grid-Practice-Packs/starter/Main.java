import java.util.*;

class Main {
    static int[][] grid(int rows, int columns) {
        // TODO: Fill the grid with row plus column.
        throw new UnsupportedOperationException("Fill the grid with row plus column");
    }

    static OptionalInt maximum(int[][] grid) {
        // TODO: Find the first and largest grid values.
        throw new UnsupportedOperationException("Find the first and largest grid values");
    }

    static boolean contains(int[][] grid, int target) {
        // TODO: Find a target in any row.
        throw new UnsupportedOperationException("Find a target in any row");
    }
    static double[][] randomGrid(int rows, int columns, Random random) {
        // TODO: Fill a rectangle with reproducible values.
        throw new UnsupportedOperationException("Fill a rectangle with reproducible values");
    }

    public static void main(String[] args) {
        int[][] values = grid(2, 3);
        System.out.println(Arrays.deepToString(values));
        System.out.println(maximum(values).orElseThrow());
    }
}
