package contacts;

import java.util.Objects;

public final class LegacyPrinter {
    private LegacyPrinter() {}
    public static String telephone(LegacyMember member) {
        Objects.requireNonNull(member, "member");
        return "(" + member.areaCode() + ") " + member.localNumber();
    }
    public static String label(LegacyMember member) {
        Objects.requireNonNull(member, "member");
        return member.displayName() + " <" + telephone(member) + ">";
    }
}
