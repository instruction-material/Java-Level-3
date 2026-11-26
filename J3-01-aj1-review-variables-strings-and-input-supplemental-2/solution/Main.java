import java.util.*;

class Main {
    static long fare(int zones) {
        if (zones < 0) throw new IllegalArgumentException("Expected nonnegative zones");
        return 2 + 3L * zones;
    }
    public static void main(String[] args) { System.out.println(fare(4)); }
}
