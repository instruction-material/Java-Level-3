import java.util.*;

class Main {
    static OptionalDouble average(int[] scores) {
        if (scores.length == 0) return OptionalDouble.empty();
        long sum = 0;
        for (int score : scores) {
            if (score < 0 || score > 100) throw new IllegalArgumentException("Expected a score from 0 to 100");
            sum += score;
        }
        return OptionalDouble.of((double) sum / scores.length);
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
