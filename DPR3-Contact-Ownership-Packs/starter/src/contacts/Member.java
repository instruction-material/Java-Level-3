package contacts;

import java.util.Objects;

public final class Member {
    private final String name;
    private String area;
    private String number;
    public Member(String name, String area, String number) {
        this.name = validName(name);
        this.area = validArea(area);
        this.number = validNumber(number);
    }
    private static String validName(String value) {
        Objects.requireNonNull(value, "name");
        if (value.isBlank() || value.codePointCount(0, value.length()) > 60
                || value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0
                || value.indexOf('<') >= 0 || value.indexOf('>') >= 0)
            throw new IllegalArgumentException("name");
        return value;
    }
    private static String validArea(String value) {
        Objects.requireNonNull(value, "area");
        if (!value.matches("[0-9]{2,4}")) throw new IllegalArgumentException("area");
        return value;
    }
    private static String validNumber(String value) {
        Objects.requireNonNull(value, "number");
        if (!value.matches("[0-9]{4,8}")) throw new IllegalArgumentException("number");
        return value;
    }
    public String displayName() { return name; }
    public String areaCode() { return area; }
    public String localNumber() { return number; }
    public String telephone() { return ContactPrinter.telephone(this); }
    public void changePhone(String nextArea, String nextNumber) {
        String checkedArea = validArea(nextArea);
        String checkedNumber = validNumber(nextNumber);
        area = checkedArea;
        number = checkedNumber;
    }
    public String maskedTelephone() {
        throw new UnsupportedOperationException("Implement optional masked format.");
    }
}
