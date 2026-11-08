package contacts;

import java.util.Objects;

public final class ContactPrinter {
    private ContactPrinter() {}
    public static String telephone(Member member) {
        Objects.requireNonNull(member, "member");
        return "(" + member.areaCode() + ") " + member.localNumber();
    }
    public static String label(Member member) {
        Objects.requireNonNull(member, "member");
        return member.displayName() + " <" + telephone(member) + ">";
    }
}
