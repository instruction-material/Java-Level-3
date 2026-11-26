public final class Main {
    private Main() {}

    public static int firstPrefix(String[] labels, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("prefix must contain text");
        }
        throw new UnsupportedOperationException("Implement firstPrefix");
    }

    public static void main(String[] args) {
        String[] labels = {"map-north", "pen", "map-south"};
        System.out.println(firstPrefix(labels, "map"));
        System.out.println(firstPrefix(labels, "pen"));
        System.out.println(firstPrefix(labels, "MAP"));
    }
}
