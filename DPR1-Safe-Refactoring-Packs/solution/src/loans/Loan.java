package loans;

import java.util.Objects;

public record Loan(String borrower, String book, int lateDays, boolean staff) {
    public Loan {
        validText(borrower, 40, "borrower");
        validText(book, 80, "book");
        validDays(lateDays);
    }
    private static void validText(String text, int maximum, String field) {
        Objects.requireNonNull(text, field);
        if (text.isBlank() || text.codePointCount(0, text.length()) > maximum
            || text.contains("\r") || text.contains("\n") || text.contains("|")) {
            throw new IllegalArgumentException(field);
        }
    }
    public static void validDays(int lateDays) {
        if (lateDays < 0 || lateDays > 365) throw new IllegalArgumentException("lateDays");
    }
}
