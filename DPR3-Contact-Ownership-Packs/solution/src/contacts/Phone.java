package contacts;

import java.util.Objects;

/** Immutable owner of phone fields, validation and presentation rules. */
public record Phone(String areaCode, String localNumber) {
    public Phone {
        Objects.requireNonNull(areaCode, "area");
        if (!areaCode.matches("[0-9]{2,4}")) throw new IllegalArgumentException("area");
        Objects.requireNonNull(localNumber, "number");
        if (!localNumber.matches("[0-9]{4,8}")) throw new IllegalArgumentException("number");
    }
    public String format() { return "(" + areaCode + ") " + localNumber; }
    public String masked() {
        return "(" + areaCode + ") " + "*".repeat(localNumber.length() - 2)
            + localNumber.substring(localNumber.length() - 2);
    }
}
