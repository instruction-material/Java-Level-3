package roster;
import java.util.Objects;
/** Shares a text capability while retaining its independent identity. */
public final class SponsorBanner implements LineSource {
    private final String name;
    public SponsorBanner(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank()) throw new IllegalArgumentException("name");
        this.name = name;
    }
    public String line() { return "Supported by " + name; }
}
