import contacts.ContactPrinter;
import contacts.Member;

/** Existing client source is kept byte-identical before and after the move. */
public final class ClientCompatibility {
    private ClientCompatibility() {}
    public static String render(Member member) {
        return ContactPrinter.label(member);
    }
    public static String readLegacyFields(Member member) {
        return member.areaCode() + "|" + member.localNumber();
    }
}
