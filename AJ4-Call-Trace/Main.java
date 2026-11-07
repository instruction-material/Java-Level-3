class Main {
    public static int sum(int n) {
        if (n < 0 || n > 100) {
            throw new IllegalArgumentException("n must be between 0 and 100");
        }
        if (n == 0) return 0;
        return n + sum(n - 1);
    }

    public static void main(String[] args) {
        System.out.println(sum(4));
    }
}
