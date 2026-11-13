import java.util.HashSet;
import java.util.List;
import supplies.LegacySupplyRequest;
import supplies.RequestCodec;
import supplies.SupplyRequest;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        String[] codes = {"PEN", "PAD", "PEN"};
        int[] units = {2, 1, 3};
        String row = "LAB|1|PEN:2;PAD:1;PEN:3";
        if (args.length > 0 && args[0].equals("base")) {
            LegacySupplyRequest request = LegacySupplyRequest.parse(row);
            System.out.println(request.toLegacy());
            int total = 0;
            for (int value : request.quantities()) total += value;
            System.out.println(request.destination() + " dispatch=" + request.dispatchMinutes()
                + " lines=" + request.itemCodes().length + " units=" + total);
            System.out.println("equal=" + request.equals(new LegacySupplyRequest("LAB", 1, codes, units)));
            System.out.println("hash-set=" + new HashSet<>(List.of(request)).contains(LegacySupplyRequest.parse(row)));
            codes[0] = "MUG";
            System.out.println("unchanged=" + request.toLegacy());
        } else {
            SupplyRequest request = new SupplyRequest("LAB", 1, codes, units);
            System.out.println(request.toLegacy());
            System.out.println(ClientCompatibility.render(request));
            System.out.println("equal=" + request.equals(RequestCodec.decode(row)));
            System.out.println("hash-set=" + new HashSet<>(List.of(request)).contains(RequestCodec.decode(row)));
            codes[0] = "MUG";
            request.quantities()[0] = 999;
            System.out.println("unchanged=" + request.toLegacy());
            if (args.length > 0 && args[0].equals("merged"))
                System.out.println("merged=" + request.merged().toLegacy());
        }
    }
}
