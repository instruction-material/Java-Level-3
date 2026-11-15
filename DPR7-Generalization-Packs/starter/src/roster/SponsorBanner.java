package roster;
import java.util.Objects;
/** Add the small LineSource interface; a sponsor is not a roster member. */
public final class SponsorBanner {
    private final String name;
    public SponsorBanner(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) throw new IllegalArgumentException("name");
        this.name = name;
    }
    public String line() { return "Supported by " + name; }
}
