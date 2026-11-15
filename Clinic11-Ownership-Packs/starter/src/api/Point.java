package api;
public record Point(int x, int y) {
    public Point {
        if (x < -100 || x > 100) throw new IllegalArgumentException("x");
        if (y < -100 || y > 100) throw new IllegalArgumentException("y");
    }
}
