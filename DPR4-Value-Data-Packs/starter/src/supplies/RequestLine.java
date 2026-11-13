package supplies;

/** TODO core: reject null code, then enforce units in 1..999. */
public record RequestLine(ItemCode code, int units) {}
