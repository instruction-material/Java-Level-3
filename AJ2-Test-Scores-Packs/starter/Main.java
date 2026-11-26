import java.util.*;

class Main {
    static OptionalDouble average(int[] scores) {
        // TODO: Calculate a nontruncated score mean.
        throw new UnsupportedOperationException("Calculate a nontruncated score mean");
    }

    static String letter(double value) {
        if (value >= 90) return "A";
        if (value >= 80) return "B";
        if (value >= 70) return "C";
        if (value >= 60) return "D";
        return "F";
    }

    public static void main(String[] args) {
        double mean = average(new int[]{89, 90}).orElseThrow();
        System.out.println(mean);
        System.out.println(letter(mean));
    }
}
