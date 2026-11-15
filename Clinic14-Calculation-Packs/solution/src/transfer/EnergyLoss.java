package transfer;
import api.EnergyResult;
import java.util.Objects;
public final class EnergyLoss {
    private EnergyLoss() {}
    public static int percentage(EnergyResult result) {
        Objects.requireNonNull(result,"result");
        return result.raw()==0?0:(int)((long)result.lost()*100/result.raw());
    }
}
