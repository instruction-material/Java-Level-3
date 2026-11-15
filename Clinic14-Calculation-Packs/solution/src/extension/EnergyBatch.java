package extension;
import api.*;
import java.util.*;
public final class EnergyBatch {
    private EnergyBatch() {}
    public static BatchTotal total(List<EnergyResult> readings) {
        List<EnergyResult> input=List.copyOf(Objects.requireNonNull(readings,"readings"));
        if(input.size()>20)throw new IllegalArgumentException("readings");
        long raw=0,usable=0,lost=0;
        for(EnergyResult result:input) {raw+=result.raw();usable+=result.usable();lost+=result.lost();}
        return new BatchTotal(raw,usable,lost);
    }
}
