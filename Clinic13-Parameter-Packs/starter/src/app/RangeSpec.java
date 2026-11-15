package app;
public record RangeSpec(int start,int end,int step) {
    public RangeSpec { throw new UnsupportedOperationException("Complete parameter object"); }
}
