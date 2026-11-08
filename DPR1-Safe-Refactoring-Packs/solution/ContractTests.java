import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import loans.Loan;
import loans.LoanDesk;
import loans.LegacyLoanDesk;

public final class ContractTests {
    private static int checks;
    private ContractTests() {}
    @FunctionalInterface private interface Fee { int amount(int days, boolean staff); }
    private static void same(Object expected, Object actual) {
        checks++;
        if (!Objects.equals(expected, actual)) throw new AssertionError(expected + " != " + actual);
    }
    private static void raises(Class<? extends Throwable> type, Runnable action) {
        checks++;
        try { action.run(); } catch (Throwable failure) {
            if (type.isInstance(failure)) return;
            throw new AssertionError("Wrong exception: " + failure, failure);
        }
        throw new AssertionError("Missing " + type.getSimpleName());
    }
    private static void core(Fee fee, Function<Loan, String> notice, Function<Loan, String> ledger) {
        for (int days = 0; days <= 365; days++) {
            for (boolean staff : new boolean[] {false, true}) {
                int expected = staff ? 0 : Math.max(0, days - 3) * 25;
                same(expected, fee.amount(days, staff));
                for (String borrower : List.of("Mira", " Mira ", "Zoë", "😀".repeat(40))) {
                    for (String book : List.of("Atlas", " A book ")) {
                        Loan loan = new Loan(borrower, book, days, staff);
                        same("To " + borrower + ": " + book + " | overdue=" + days + " | charge=" + expected,
                             notice.apply(loan));
                        same(borrower + "|" + book + "|" + days + "|" + expected + "|" + (staff ? "STAFF" : "READER"),
                             ledger.apply(loan));
                        same(borrower, loan.borrower());
                        same(book, loan.book());
                    }
                }
            }
        }
        raises(NullPointerException.class, () -> notice.apply(null));
        raises(NullPointerException.class, () -> ledger.apply(null));
        for (int bad : new int[] {-1, 366, Integer.MIN_VALUE, Integer.MAX_VALUE}) {
            raises(IllegalArgumentException.class, () -> fee.amount(bad, false));
            raises(IllegalArgumentException.class, () -> fee.amount(bad, true));
            raises(IllegalArgumentException.class, () -> new Loan("M", "B", bad, false));
        }
        for (String bad : List.of("", " ", "\t", "a\nb", "a\rb", "a|b")) {
            raises(IllegalArgumentException.class, () -> new Loan(bad, "Book", 0, false));
            raises(IllegalArgumentException.class, () -> new Loan("Mira", bad, 0, false));
        }
        raises(NullPointerException.class, () -> new Loan(null, null, -1, false));
        raises(NullPointerException.class, () -> new Loan("M", null, -1, false));
        raises(IllegalArgumentException.class, () -> new Loan("😀".repeat(41), "B", 0, false));
        raises(IllegalArgumentException.class, () -> new Loan("M", "x".repeat(81), 0, false));
        same(80, new Loan("M", "x".repeat(80), 0, false).book().length());
        Loan repeated = new Loan("Mira", "Atlas", 365, false);
        same(9050, fee.amount(365, false));
        same(notice.apply(repeated), notice.apply(repeated));
        same(ledger.apply(repeated), ledger.apply(repeated));
    }
    private static void optional() {
        Loan first = new Loan("Mira", "Atlas", 4, false);
        Loan second = new Loan("Sol", "Maps", 12, true);
        ArrayList<Loan> input = new ArrayList<>(List.of(first, second, first));
        List<String> result = LoanDesk.noticeAll(input);
        same(List.of(
            "To Mira: Atlas | overdue=4 | charge=25",
            "To Sol: Maps | overdue=12 | charge=0",
            "To Mira: Atlas | overdue=4 | charge=25"), result);
        same(List.of(first, second, first), input);
        raises(UnsupportedOperationException.class, () -> result.add("changed"));
        input.clear();
        same(3, result.size());
        same(List.of(), LoanDesk.noticeAll(List.of()));
        raises(NullPointerException.class, () -> LoanDesk.noticeAll(null));
        ArrayList<Loan> invalid = new ArrayList<>();
        invalid.add(first); invalid.add(null);
        raises(NullPointerException.class, () -> LoanDesk.noticeAll(invalid));
        same(2, invalid.size());
        same("To Mira: Atlas | overdue=4 | charge=25", LoanDesk.notice(first));
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!List.of("base", "core", "all").contains(mode)) throw new IllegalArgumentException("mode");
        core(LegacyLoanDesk::fee, LegacyLoanDesk::notice, LegacyLoanDesk::ledger);
        if (!mode.equals("base")) core(LoanDesk::fee, LoanDesk::notice, LoanDesk::ledger);
        if (mode.equals("all")) optional();
        String label = mode.equals("base") ? "Baseline" : mode.equals("core") ? "Core" : "Complete";
        System.out.println(label + " safe-refactoring contracts: " + checks + " passed");
    }
}
