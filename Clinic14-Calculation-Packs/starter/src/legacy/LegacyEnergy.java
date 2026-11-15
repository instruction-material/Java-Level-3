package legacy;
import api.*;
public final class LegacyEnergy implements EnergyAPI {
    public LegacyEnergy() {}
    public EnergyResult measure(int watts,int seconds,int efficiency) {
        EnergyRules.watts(watts);EnergyRules.seconds(seconds);EnergyRules.efficiency(efficiency);
        int value=watts*seconds;
        int original=value;
        value=value*efficiency/100;
        return new EnergyResult(original,value,original-value);
    }
}
