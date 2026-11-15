package api;
public record EnergyResult(int raw,int usable,int lost) {
    public EnergyResult {
        if(raw<0||raw>3600000)throw new IllegalArgumentException("raw");
        if(usable<0||usable>raw)throw new IllegalArgumentException("usable");
        if(lost!=raw-usable)throw new IllegalArgumentException("lost");
    }
}
