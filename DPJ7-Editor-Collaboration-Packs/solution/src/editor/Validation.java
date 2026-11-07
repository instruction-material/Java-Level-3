// Chain of Responsibility: ordered checks stop at the first rejection.
package editor;
import java.util.List;
import java.util.Objects;

public final class Validation {
    public record Verdict(boolean accepted, String reason) {
        public Verdict {
            reason = TextDocument.line(reason);
            if (accepted ? !reason.isEmpty() : reason.isBlank())
                throw new IllegalArgumentException("invalid verdict reason");
        }
        public static Verdict pass() { return new Verdict(true, ""); }
        public static Verdict fail(String reason) { return new Verdict(false, reason); }
    }
    @FunctionalInterface public interface Check { Verdict check(String text); }
    private final List<Check> checks;
    public Validation(List<Check> checks) { this.checks = List.copyOf(checks); }
    public Verdict check(String text) {
        String valid = TextDocument.line(text);
        for (Check check : checks) {
            Verdict result = Objects.requireNonNull(check.check(valid), "verdict");
            if (!result.accepted()) return result;
        }
        return Verdict.pass();
    }
    public static Check nonBlank() {
        return text -> text.isBlank() ? Verdict.fail("blank") : Verdict.pass();
    }
    public static Check maxLength(int limit) {
        if (limit < 0) throw new IllegalArgumentException("negative limit");
        return text -> text.length() > limit ? Verdict.fail("too long") : Verdict.pass();
    }
}
