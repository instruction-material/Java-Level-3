import java.util.*;

class Main {
    static ArrayList<Integer> evens(List<Integer> values) {
        var result = new ArrayList<Integer>();
        for (int value : values) {
            if (value % 2 == 0) result.add(value);
        }
        return result;
    }

    static boolean hasZeroPair(List<Integer> values) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                if ((long) values.get(i) + values.get(j) == 0) return true;
            }
        }
        return false;
    }

    static void removeFirstMinimum(ArrayList<Integer> values) {
        if (values.isEmpty()) return;
        int smallest = 0;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) < values.get(smallest)) smallest = i;
        }
        values.remove(smallest);
    }

    static void appendTotal(ArrayList<Integer> values) {
        long total = 0;
        for (int value : values) total += value;
        values.add(Math.toIntExact(total));
    }
    public static void main(String[] args) {
        System.out.println(evens(List.of(5, 2, -4, 2, 7)));
    }
}
