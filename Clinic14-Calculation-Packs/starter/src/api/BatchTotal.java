package api;
public record BatchTotal(long raw,long usable,long lost) {
    public BatchTotal {
        if(raw<0||raw>72000000)throw new IllegalArgumentException("raw");
        if(usable<0||usable>raw)throw new IllegalArgumentException("usable");
        if(lost!=raw-usable)throw new IllegalArgumentException("lost");
    }
}
