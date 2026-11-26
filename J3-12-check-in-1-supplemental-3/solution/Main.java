public class Main {
    static long visits(int size) {
        if (size < 0) throw new IllegalArgumentException("Size must be nonnegative.");
        return 2L * size * (size / 4);
    }

    public static void main(String[] args) {
        System.out.println(visits(3));
        System.out.println(visits(4));
        System.out.println(visits(8));
    }
}
