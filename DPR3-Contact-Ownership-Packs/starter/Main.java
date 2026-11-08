import java.time.LocalDate;
import contacts.ContactPrinter;
import contacts.LegacyMember;
import contacts.LegacyPrinter;
import contacts.Member;
import dates.BusinessDate;
import dates.ForeignDateMethods;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("base")) {
            LegacyMember member = new LegacyMember("Mira", "212", "5550100");
            System.out.println(member.telephone());
            System.out.println(LegacyPrinter.label(member));
            member.changePhone("020", "70001234");
            System.out.println(LegacyPrinter.label(member));
            System.out.println(member.areaCode() + "|" + member.localNumber());
        } else {
            Member member = new Member("Mira", "212", "5550100");
            System.out.println(ContactPrinter.telephone(member));
            System.out.println(ClientCompatibility.render(member));
            member.changePhone("020", "70001234");
            System.out.println(ClientCompatibility.render(member));
            System.out.println(ClientCompatibility.readLegacyFields(member));
        }
        LocalDate monday = LocalDate.of(2024, 1, 1);
        System.out.println(ForeignDateMethods.followingMonday(monday));
        System.out.println(new BusinessDate(monday).followingMonday().date());
    }
}
