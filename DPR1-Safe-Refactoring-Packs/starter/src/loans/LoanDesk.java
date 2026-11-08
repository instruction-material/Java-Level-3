package loans;

import java.util.Objects;
import java.util.List;

public final class LoanDesk {
    private LoanDesk() {}
    public static int fee(int lateDays, boolean staff) {
        Loan.validDays(lateDays);
        int cost = 0;
        if (!staff) {
            if (lateDays > 3) cost = (lateDays - 3) * 25;
        }
        return cost;
    }
    public static String notice(Loan loan) {
        Objects.requireNonNull(loan, "loan");
        int cost = 0;
        if (!loan.staff()) {
            if (loan.lateDays() > 3) cost = (loan.lateDays() - 3) * 25;
        }
        return "To " + loan.borrower() + ": " + loan.book()
            + " | overdue=" + loan.lateDays() + " | charge=" + cost;
    }
    public static String ledger(Loan loan) {
        Objects.requireNonNull(loan, "loan");
        int cost = 0;
        if (!loan.staff()) {
            if (loan.lateDays() > 3) cost = (loan.lateDays() - 3) * 25;
        }
        return loan.borrower() + "|" + loan.book() + "|" + loan.lateDays()
            + "|" + cost + "|" + (loan.staff() ? "STAFF" : "READER");
    }

    public static List<String> noticeAll(List<Loan> loans) {
        throw new UnsupportedOperationException("Implement optional immutable notice batch.");
    }
}
