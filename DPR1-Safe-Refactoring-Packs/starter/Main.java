import loans.Loan;
import loans.LoanDesk;
import loans.LegacyLoanDesk;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        boolean base = args.length > 0 && args[0].equals("base");
        Loan loan = new Loan("Mira", "Atlas", 4, false);
        System.out.println(base ? LegacyLoanDesk.fee(4, false) : LoanDesk.fee(4, false));
        System.out.println(base ? LegacyLoanDesk.notice(loan) : LoanDesk.notice(loan));
        System.out.println(base ? LegacyLoanDesk.ledger(loan) : LoanDesk.ledger(loan));
        System.out.println(base ? LegacyLoanDesk.fee(365, true) : LoanDesk.fee(365, true));
    }
}
