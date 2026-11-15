import app.EnergyMeter;
import legacy.LegacyEnergy;
import api.*;
import transfer.EnergyLoss;
import extension.EnergyBatch;
import java.util.List;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> System.out.println(new LegacyEnergy().measure(33,3,50));
            case "core" -> System.out.println(new EnergyMeter().measure(33,3,50));
            case "transfer" -> System.out.println(EnergyLoss.percentage(new EnergyResult(99,49,50)));
            case "extension" -> System.out.println(EnergyBatch.total(List.of(new EnergyResult(99,49,50),new EnergyResult(100,100,0))));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
