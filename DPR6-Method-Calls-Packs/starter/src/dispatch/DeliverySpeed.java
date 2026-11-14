package dispatch;
public enum DeliverySpeed {
    STANDARD(0), EXPRESS(250);
    private final int surcharge;
    DeliverySpeed(int surcharge) { this.surcharge = surcharge; }
    public int surchargeCents() { return surcharge; }
}
