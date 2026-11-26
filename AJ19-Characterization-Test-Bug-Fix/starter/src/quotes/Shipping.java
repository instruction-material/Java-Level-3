package quotes;

public final class Shipping {
    public int charge(int subtotalCents) {
        if (subtotalCents < 0) throw new IllegalArgumentException("Negative subtotal");
        // TODO: Make the free-shipping threshold inclusive.
        return subtotalCents > 5_000 ? 0 : 250;
    }
}
