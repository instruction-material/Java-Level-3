import java.util.HashMap;

public class Main {
    static boolean valid(int[] input, int[] candidate) {
        if (input.length != candidate.length) return false;
        var counts = new HashMap<Integer, Integer>();
        for (int number : input) counts.merge(number, 1, Integer::sum);
        for (int i = 0; i < candidate.length; i++) {
            if (i > 0 && candidate[i - 1] > candidate[i]) return false;
            int number = candidate[i];
            int available = counts.getOrDefault(number, 0);
            if (available == 0) return false;
            if (available == 1) counts.remove(number);
            else counts.put(number, available - 1);
        }
        return counts.isEmpty();
    }

    public static void main(String[] args) {
        int[] input = {7, -3, 7, 1};
        System.out.println(valid(input, new int[]{-3, 1, 7, 7}));
        System.out.println(valid(input, new int[]{-3, 1, 1, 7}));
    }
}
