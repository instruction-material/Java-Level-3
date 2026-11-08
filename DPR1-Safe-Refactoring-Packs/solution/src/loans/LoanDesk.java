package loans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LoanDesk {
    private static final int FREE_DAYS = 3;
    private static final int DAILY_CENTS = 25;
    private LoanDesk() {}
    public static int fee(int lateDays, boolean staff) {
        Loan.validDays(lateDays);
        return staff ? 0 : Math.max(0, lateDays - FREE_DAYS) * DAILY_CENTS;
    }
    public static String notice(Loan loan) {
        Objects.requireNonNull(loan, "loan");
        return "To " + loan.borrower() + ": " + loan.book()
            + " | overdue=" + loan.lateDays() + " | charge=" + fee(loan.lateDays(), loan.staff());
    }
    public static String ledger(Loan loan) {
        Objects.requireNonNull(loan, "loan");
        return loan.borrower() + "|" + loan.book() + "|" + loan.lateDays()
            + "|" + fee(loan.lateDays(), loan.staff()) + "|" + (loan.staff() ? "STAFF" : "READER");
    }
    public static List<String> noticeAll(List<Loan> loans) {
        List<Loan> snapshot = List.copyOf(loans);
        List<String> notices = new ArrayList<>();
        for (Loan loan : snapshot) notices.add(notice(loan));
        return List.copyOf(notices);
    }
}
