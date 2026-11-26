public final class Main {
    public record Result(int index, int comparisons) {}

    private Main() {}

    public static Result search(int[] values, int target) {
        throw new UnsupportedOperationException("Implement search");
    }

    public static void main(String[] args) {
        for (int size : new int[]{8, 16, 32}) {
            int[] values = new int[size];
            for (int i = 0; i < size; i++) values[i] = i;
            System.out.printf("%d %d %d %d%n", size, search(values, 0).comparisons(),
                search(values, size - 1).comparisons(), search(values, -1).comparisons());
        }
    }
}
