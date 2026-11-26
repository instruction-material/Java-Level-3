import java.util.*;

class Main {
    static int[][] add(int[][] left, int[][] right) {
        if (left.length != right.length) throw new IllegalArgumentException("Different row counts");
        int columns = left.length == 0 ? 0 : left[0].length;
        int[][] result = new int[left.length][columns];
        for (int row = 0; row < left.length; row++) {
            if (left[row].length != columns || right[row].length != columns) {
                throw new IllegalArgumentException("Expected matching rectangular dimensions");
            }
            for (int column = 0; column < columns; column++) {
                result[row][column] = Math.addExact(left[row][column], right[row][column]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(add(new int[][]{{1, 2}, {3, 4}}, new int[][]{{4, 3}, {2, 1}})));
    }
}
