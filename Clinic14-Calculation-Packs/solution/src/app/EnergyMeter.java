package app;
import api.*;
public final class EnergyMeter implements EnergyAPI {
    public EnergyMeter() {}
    public EnergyResult measure(int watts,int seconds,int efficiency) {
        int raw=raw(watts,seconds);
        int usable=usable(raw,efficiency);
        return new EnergyResult(raw,usable,raw-usable);
    }
    public int raw(int watts,int seconds) {
        EnergyRules.watts(watts);EnergyRules.seconds(seconds);
        return watts*seconds;
    }
    public int usable(int raw,int efficiency) {
        if(raw<0||raw>3600000)throw new IllegalArgumentException("raw");
        EnergyRules.efficiency(efficiency);
        return raw*efficiency/100;
    }
}
