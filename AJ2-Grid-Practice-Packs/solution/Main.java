import java.util.*;

class Main {
    static int[][] grid(int rows, int columns) {
        if (rows < 0 || columns < 0) throw new IllegalArgumentException("Negative dimension");
        int[][] result = new int[rows][columns];
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) result[row][column] = row + column;
        }
        return result;
    }

    static OptionalInt maximum(int[][] grid) {
        OptionalInt best = OptionalInt.empty();
        for (int[] row : grid) {
            for (int value : row) {
                if (best.isEmpty() || value > best.getAsInt()) best = OptionalInt.of(value);
            }
        }
        return best;
    }

    static boolean contains(int[][] grid, int target) {
        for (int[] row : grid) {
            for (int value : row) if (value == target) return true;
        }
        return false;
    }
    static double[][] randomGrid(int rows, int columns, Random random) {
        if (rows < 0 || columns < 0) throw new IllegalArgumentException("Negative dimension");
        double[][] result = new double[rows][columns];
        for (double[] row : result) {
            for (int column = 0; column < row.length; column++) row[column] = random.nextDouble(10);
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] values = grid(2, 3);
        System.out.println(Arrays.deepToString(values));
        System.out.println(maximum(values).orElseThrow());
    }
}
