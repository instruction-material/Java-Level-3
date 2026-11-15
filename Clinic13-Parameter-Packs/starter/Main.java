import app.RangeService;
import legacy.LegacyRanges;
import transfer.EveryOther;
import extension.MergeRanges;
import java.util.List;
public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String mode=args.length==0?"core":args[0];
        switch(mode) {
            case "baseline" -> System.out.println(new LegacyRanges().describe(2,9,3));
            case "core" -> System.out.println(new RangeService().describe(2,9,3));
            case "transfer" -> System.out.println(EveryOther.select(List.of(2,5,8)));
            case "extension" -> System.out.println(MergeRanges.unique(List.of(2,5,8),List.of(1,5,9)));
            default -> throw new IllegalArgumentException("mode");
        }
    }
}
