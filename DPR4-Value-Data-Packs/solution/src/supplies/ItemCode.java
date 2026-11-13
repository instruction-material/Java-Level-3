package supplies;

/** Exact case-sensitive value; retain the supplied String without normalization. */
public record ItemCode(String value) {
    public ItemCode {
        if (value == null || !value.matches("[A-Z][A-Z0-9]{0,7}"))
            throw new IllegalArgumentException("code");
    }
}
