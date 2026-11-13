package supplies;

public record RequestLine(ItemCode code, int units) {
    public RequestLine {
        if (code == null) throw new IllegalArgumentException("code");
        if (units < 1 || units > 999) throw new IllegalArgumentException("units");
    }
}
