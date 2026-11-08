package checkout;
public record Totals(int subtotal,int shipping) {
    public Totals {
        if(subtotal<0 || shipping<0)throw new IllegalArgumentException("negative amount");
    }
    public int total(){ return Math.addExact(subtotal,shipping); }
}
