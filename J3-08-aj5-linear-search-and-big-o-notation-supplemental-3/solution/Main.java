public final class Main {
    public record Result(int index, int comparisons) {}

    private Main() {}

    public static Result search(int[] values, int target) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == target) return new Result(i, i + 1);
        }
        return new Result(-1, values.length);
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
