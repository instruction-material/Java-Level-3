package extension;
import api.*;
import java.util.List;
public final class EnergyBatch {
    private EnergyBatch() {}
    public static BatchTotal total(List<EnergyResult> readings) {throw new UnsupportedOperationException("Complete batch total");}
}
