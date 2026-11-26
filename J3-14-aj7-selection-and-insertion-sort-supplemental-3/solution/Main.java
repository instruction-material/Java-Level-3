public class Main {
    static long inversions(int[] values) {
        long count = 0;
        for (int earlier = 0; earlier < values.length; earlier++) {
            for (int later = earlier + 1; later < values.length; later++) {
                if (values[earlier] > values[later]) count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(inversions(new int[]{4, 1, 3, 1}));
        System.out.println(inversions(new int[]{1, 1, 3, 4}));
    }
}
