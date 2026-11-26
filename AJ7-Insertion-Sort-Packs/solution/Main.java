import java.util.Arrays;

public class Main {
    static long sort(int[] values) {
        long shifts = 0;
        for (int next = 1; next < values.length; next++) {
            int saved = values[next];
            int position = next;
            while (position > 0 && values[position - 1] > saved) {
                values[position] = values[position - 1];
                position--;
                shifts++;
            }
            values[position] = saved;
        }
        return shifts;
    }

    public static void main(String[] args) {
        int[] values = {4, 1, 3, 1};
        long shifts = sort(values);
        System.out.println(Arrays.toString(values));
        System.out.println("Shifts: " + shifts);
    }
}
