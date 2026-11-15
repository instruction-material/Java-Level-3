package journal;
import java.util.Objects;
/** Optional characterization exercise: mask only the two documented volatile fields. */
public final class CaptureNormalizer {
    private CaptureNormalizer() {}
    public static String normalize(String line) {
        // TODO: validate a complete journal line, then mask time and ID only.
        return Objects.requireNonNull(line, "line");
    }
}
