package app;
import api.*;
import legacy.LegacyEnergy;
public final class EnergyMeter implements EnergyAPI {
    public EnergyMeter() {}
    public EnergyResult measure(int watts,int seconds,int efficiency) {
        EnergyRules.watts(watts);EnergyRules.seconds(seconds);EnergyRules.efficiency(efficiency);
        int value=watts*seconds;
        int original=value;
        value=value*efficiency/100;
        return new EnergyResult(original,value,original-value);
    }
    public int raw(int watts,int seconds) {throw new UnsupportedOperationException("Complete named queries");}
    public int usable(int raw,int efficiency) {throw new UnsupportedOperationException("Complete named queries");}
}
