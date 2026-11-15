import api.*;
import app.EnergyMeter;
import legacy.LegacyEnergy;
import transfer.EnergyLoss;
import extension.EnergyBatch;
import java.util.*;
import java.util.function.Supplier;
import static check.Checks.*;
public final class ContractChecks {
    private ContractChecks() {}
    private static void behavior(Supplier<EnergyAPI> make) {
        EnergyAPI energy=make.get();
        same(new EnergyResult(99,49,50),energy.measure(33,3,50),"round after multiplying");
        for(int watts:new int[]{0,1,33,999,1000})for(int seconds:new int[]{0,1,3,59,3600})
            for(int efficiency:new int[]{0,1,50,99,100}) {
                long raw=(long)watts*seconds,usable=raw*efficiency/100;
                same(new EnergyResult((int)raw,(int)usable,(int)(raw-usable)),energy.measure(watts,seconds,efficiency),"widened model");
            }
        fails(IllegalArgumentException.class,"watts",()->energy.measure(-1,-1,-1));
        fails(IllegalArgumentException.class,"watts",()->energy.measure(1001,0,0));
        fails(IllegalArgumentException.class,"seconds",()->energy.measure(0,3601,-1));
        fails(IllegalArgumentException.class,"efficiency",()->energy.measure(0,0,101));
        fails(IllegalArgumentException.class,"raw",()->new EnergyResult(-1,0,0));
        fails(IllegalArgumentException.class,"usable",()->new EnergyResult(1,2,0));
        fails(IllegalArgumentException.class,"lost",()->new EnergyResult(2,1,0));
    }
    private static void core() {
        behavior(EnergyMeter::new);EnergyMeter meter=new EnergyMeter();
        same(99,meter.raw(33,3),"raw query");same(49,meter.usable(99,50),"usable query");
        same(3600000,meter.raw(1000,3600),"raw maximum");
        same(3600000,meter.usable(3600000,100),"usable maximum");
        same(0,meter.usable(0,0),"zero query");
        fails(IllegalArgumentException.class,"raw",()->meter.usable(-1,-1));
        fails(IllegalArgumentException.class,"raw",()->meter.usable(3600001,0));
        fails(IllegalArgumentException.class,"efficiency",()->meter.usable(0,-1));
        fails(IllegalArgumentException.class,"watts",()->meter.raw(-1,-1));
        fails(IllegalArgumentException.class,"seconds",()->meter.raw(0,-1));
    }
    private static void transfer() {
        same(50,EnergyLoss.percentage(new EnergyResult(99,49,50)),"fractional percentage floors");
        same(0,EnergyLoss.percentage(new EnergyResult(0,0,0)),"zero energy policy");
        for(int raw=1;raw<=35;raw++)for(int usable=0;usable<=raw;usable++)
            same((int)((long)(raw-usable)*100/raw),EnergyLoss.percentage(new EnergyResult(raw,usable,raw-usable)),"loss fraction");
        same(100,EnergyLoss.percentage(new EnergyResult(3600000,0,3600000)),"maximum");
        fails(NullPointerException.class,"result",()->EnergyLoss.percentage(null));
    }
    private static void extension() {
        List<EnergyResult> input=new ArrayList<>(List.of(new EnergyResult(99,49,50),new EnergyResult(100,100,0)));
        same(new BatchTotal(199,149,50),EnergyBatch.total(input),"sum every field");
        same(2,input.size(),"input retained");
        same(new BatchTotal(0,0,0),EnergyBatch.total(List.of()),"empty total");
        same(new BatchTotal(72000000,0,72000000),EnergyBatch.total(Collections.nCopies(20,new EnergyResult(3600000,0,3600000))),"batch maximum");
        fails(NullPointerException.class,"readings",()->EnergyBatch.total(null));
        fails(IllegalArgumentException.class,"readings",()->EnergyBatch.total(Collections.nCopies(21,new EnergyResult(0,0,0))));
        List<EnergyResult> bad=new ArrayList<>();bad.add(null);
        try{EnergyBatch.total(bad);throw new AssertionError("null reading");}
        catch(NullPointerException expected){yes(true,"null reading rejected");}
    }
    public static void main(String[] args) {
        String mode=args.length==0?"all":args[0];
        switch(mode) {
            case "baseline" -> {behavior(LegacyEnergy::new);behavior(EnergyMeter::new);}
            case "core" -> core();
            case "transfer" -> transfer();
            case "extension" -> extension();
            case "all" -> {core();transfer();extension();}
            default -> throw new IllegalArgumentException("mode");
        }
        System.out.println(mode+": "+count()+" checks passed");
    }
}
