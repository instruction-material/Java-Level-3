package dispatch;
import java.util.ArrayList;
import java.util.List;

/** Frozen original API and behavior oracle. Money is integer cents. */
public final class LegacyDesk {
    private int discount;
    private final List<Quote> sent = new ArrayList<>();
    public LegacyDesk(int discountCents) { setDiscount(discountCents); }
    public void setDiscount(int discountCents) {
        if (discountCents < 0 || discountCents > 100)
            throw new IllegalArgumentException("discountCents");
        discount = discountCents;
    }
    public int price(String label, int grams, int zone, boolean express, String unused) {
        if (label == null || label.isBlank() || grams < 1 || grams > 5000
                || zone < 1 || zone > 3) return -1;
        int cents = zone * 100 + ((grams + 499) / 500) * 40
                + (express ? 250 : 0) - discount;
        sent.add(new Quote(label, cents));
        return cents;
    }
    public Quote find(String label) {
        if (label == null || label.isBlank()) return null;
        for (int i = sent.size() - 1; i >= 0; i--)
            if (sent.get(i).label().equals(label)) return sent.get(i);
        return null;
    }
    public int totalCents() { return sent.stream().mapToInt(Quote::cents).sum(); }
    public List<Quote> history() { return List.copyOf(sent); }
}
