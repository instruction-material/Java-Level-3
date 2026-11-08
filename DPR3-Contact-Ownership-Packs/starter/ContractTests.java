import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import contacts.ContactPrinter;
import contacts.LegacyMember;
import contacts.LegacyPrinter;
import contacts.Member;
import dates.BusinessDate;
import dates.ForeignDateMethods;

public final class ContractTests {
    private static int checks;
    private ContractTests() {}
    private interface View {
        String name(); String area(); String number(); String telephone(); String label();
        void change(String area, String number);
    }
    @FunctionalInterface private interface Make { View create(String name, String area, String number); }
    private static View legacy(String name, String area, String number) {
        LegacyMember member = new LegacyMember(name, area, number);
        return new View() {
            public String name() { return member.displayName(); }
            public String area() { return member.areaCode(); }
            public String number() { return member.localNumber(); }
            public String telephone() { return member.telephone(); }
            public String label() { return LegacyPrinter.label(member); }
            public void change(String a, String n) { member.changePhone(a, n); }
        };
    }
    private static View target(String name, String area, String number) {
        Member member = new Member(name, area, number);
        return new View() {
            public String name() { return member.displayName(); }
            public String area() { return member.areaCode(); }
            public String number() { return member.localNumber(); }
            public String telephone() {
                same(member.telephone(), ContactPrinter.telephone(member));
                same(member.areaCode() + "|" + member.localNumber(),
                     ClientCompatibility.readLegacyFields(member));
                return member.telephone();
            }
            public String label() { return ClientCompatibility.render(member); }
            public void change(String a, String n) { member.changePhone(a, n); }
        };
    }
    private static void same(Object expected, Object actual) {
        checks++;
        if (!expected.equals(actual)) throw new AssertionError("Expected " + expected + "; got " + actual);
    }
    private static void identity(Object expected, Object actual) {
        checks++;
        if (expected != actual) throw new AssertionError("Input text identity changed");
    }
    private static void raises(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable failure) {
            if (type.isInstance(failure)) return;
            throw new AssertionError("Wrong exception: " + failure, failure);
        }
        throw new AssertionError("Missing " + type.getSimpleName());
    }
    private static void state(View view, String name, String area, String number) {
        same(name, view.name()); same(area, view.area()); same(number, view.number());
        same("(" + area + ") " + number, view.telephone());
        same(name + " <(" + area + ") " + number + ">", view.label());
    }
    private static void ownership(Make make) {
        for (String name : List.of("Mira", " Mira ", "Zoë", "😀".repeat(60))) {
            for (String area : List.of("02", "020", "0000", "212")) {
                for (String number : List.of("0000", "0123", "5550100", "12345678")) {
                    View view = make.create(name, area, number);
                    state(view, name, area, number);
                    identity(name, view.name()); identity(area, view.area()); identity(number, view.number());
                    view.change("030", "01234567"); state(view, name, "030", "01234567");
                    view.change(area, number); state(view, name, area, number);
                }
            }
        }
        for (String bad : List.of("", " ", "a\nb", "a\rb", "<Mira", "Mira>", "😀".repeat(61)))
            raises(IllegalArgumentException.class, () -> make.create(bad, null, null));
        raises(NullPointerException.class, () -> make.create(null, "bad", "bad"));
        raises(NullPointerException.class, () -> make.create("Mira", null, "bad"));
        raises(NullPointerException.class, () -> make.create("Mira", "212", null));
        for (String bad : List.of("", "1", "12345", " 212", "212 ", "２１２", "+212", "2\n12"))
            raises(IllegalArgumentException.class, () -> make.create("Mira", bad, "5550100"));
        for (String bad : List.of("", "123", "123456789", " 1234", "1234 ", "１２３４", "12-34", "12\n34"))
            raises(IllegalArgumentException.class, () -> make.create("Mira", "212", bad));
        View view = make.create("Mira", "212", "5550100");
        for (String bad : List.of("", "1", "12345", "２１２")) {
            raises(IllegalArgumentException.class, () -> view.change(bad, "70001234"));
            state(view, "Mira", "212", "5550100");
        }
        for (String bad : List.of("", "123", "123456789", "１２３４")) {
            raises(IllegalArgumentException.class, () -> view.change("020", bad));
            state(view, "Mira", "212", "5550100");
        }
        raises(NullPointerException.class, () -> view.change(null, "bad"));
        state(view, "Mira", "212", "5550100");
        raises(NullPointerException.class, () -> view.change("020", null));
        state(view, "Mira", "212", "5550100");
        view.change("020", "70001234"); state(view, "Mira", "020", "70001234");
        view.change("020", "70001234"); state(view, "Mira", "020", "70001234");
    }
    private static void dates() {
        for (int day = 0; day < 400; day++) {
            LocalDate input = LocalDate.of(2023, 12, 25).plusDays(day);
            LocalDate expected = input.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            same(expected, ForeignDateMethods.followingMonday(input));
            BusinessDate wrapped = new BusinessDate(input);
            identity(input, wrapped.date());
            same(expected, wrapped.followingMonday().date());
            same(input, wrapped.date());
            same(wrapped, new BusinessDate(input));
        }
        raises(NullPointerException.class, () -> ForeignDateMethods.followingMonday(null));
        raises(NullPointerException.class, () -> new BusinessDate(null));
        raises(DateTimeException.class, () -> ForeignDateMethods.followingMonday(LocalDate.MAX));
        raises(DateTimeException.class, () -> new BusinessDate(LocalDate.MAX).followingMonday());
        same(LocalDate.MIN.with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
             ForeignDateMethods.followingMonday(LocalDate.MIN));
    }
    private static void optional() {
        for (String area : List.of("02", "020", "0000", "212")) {
            for (String number : List.of("0000", "0123", "5550100", "12345678")) {
                Member member = new Member("Mira", area, number);
                String expected = "(" + area + ") " + "*".repeat(number.length() - 2)
                    + number.substring(number.length() - 2);
                same(expected, member.maskedTelephone());
                same(number, member.localNumber());
                same("(" + area + ") " + number, ContactPrinter.telephone(member));
                member.changePhone("030", "01234567");
                same("(030) ******67", member.maskedTelephone());
                same("Mira <(030) 01234567>", ClientCompatibility.render(member));
            }
        }
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!List.of("base", "core", "all").contains(mode)) throw new IllegalArgumentException("mode");
        ownership(ContractTests::legacy);
        if (!mode.equals("base")) {
            ownership(ContractTests::target);
            raises(NullPointerException.class, () -> ContactPrinter.telephone(null));
            raises(NullPointerException.class, () -> ContactPrinter.label(null));
        }
        dates();
        if (mode.equals("all")) optional();
        String label = mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core" : "Complete";
        System.out.println(label + " ownership-move contracts: " + checks + " passed");
    }
}
