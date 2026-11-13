import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import supplies.ItemCode;
import supplies.LegacySupplyRequest;
import supplies.RequestCodec;
import supplies.RequestLine;
import supplies.ServiceLevel;
import supplies.SupplyRequest;

public final class ContractTests {
    private static int checks;
    private ContractTests() {}
    private static void check(boolean condition, String label) {
        checks++;
        if (!condition) throw new AssertionError(label);
    }
    private static void same(Object expected, Object actual, String label) {
        check(Objects.equals(expected, actual), label + ": expected=" + expected + " actual=" + actual);
    }
    private static void invalid(Runnable action, String message) {
        checks++;
        try { action.run(); }
        catch (IllegalArgumentException error) {
            if (!message.equals(error.getMessage())) throw new AssertionError("error order/message: " + error);
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException: " + message);
    }
    private static void immutable(Runnable action) {
        checks++;
        try { action.run(); }
        catch (UnsupportedOperationException expected) { return; }
        throw new AssertionError("List mutation was accepted");
    }
    private static void baseline() {
        for (String destination : List.of("LAB", "OFFICE", "STUDIO")) {
            for (int service = 0; service < 3; service++) {
                for (int size = 0; size <= 4; size++) {
                    for (int quantity : new int[] {1, 2, 999}) {
                        String[] codes = new String[size];
                        int[] units = new int[size];
                        for (int i = 0; i < size; i++) {
                            codes[i] = i % 2 == 0 ? "PEN" : "PAD";
                            units[i] = quantity;
                        }
                        LegacySupplyRequest legacy = new LegacySupplyRequest(destination, service, codes, units);
                        SupplyRequest current = new SupplyRequest(destination, service, codes, units);
                        same(legacy.toLegacy(), current.toLegacy(), "wire bytes");
                        same(legacy.destination(), current.destination(), "destination");
                        same(legacy.serviceCode(), current.serviceCode(), "service");
                        same(legacy.dispatchMinutes(), current.dispatchMinutes(), "dispatch");
                        same(legacy.hashCode(), current.hashCode(), "stable hash calculation");
                        same(size, current.itemCodes().length, "line count");
                        same(legacy.toLegacy(), RequestCodec.decode(legacy.toLegacy()).toLegacy(), "decode");
                        same(legacy.toLegacy(), RequestCodec.encode(current), "encode");
                        check(current.equals(RequestCodec.decode(current.toLegacy())), "value equality");
                        check(new HashSet<>(List.of(current)).contains(RequestCodec.decode(current.toLegacy())), "set lookup");
                        String frozen = current.toLegacy();
                        if (size > 0) {
                            codes[0] = "MUG";
                            units[0] = 7;
                            current.itemCodes()[0] = "BOX";
                            current.quantities()[0] = 8;
                        }
                        same(frozen, current.toLegacy(), "constructor/getter ownership");
                        check(ClientCompatibility.render(current).startsWith(destination + " dispatch="), "unchanged client");
                    }
                }
            }
        }
        invalid(() -> new SupplyRequest(null, -1, null, null), "destination");
        invalid(() -> new SupplyRequest("LAB", -1, null, null), "service");
        invalid(() -> new SupplyRequest("LAB", 0, null, null), "items");
        invalid(() -> new SupplyRequest("LAB", 0, new String[]{"PEN"}, new int[0]), "items");
        invalid(() -> new SupplyRequest("LAB", 0, new String[]{null}, new int[]{0}), "code");
        invalid(() -> new SupplyRequest("LAB", 0, new String[]{"pen"}, new int[]{1}), "code");
        invalid(() -> new SupplyRequest("LAB", 0, new String[]{"PEN"}, new int[]{0}), "units");
        invalid(() -> new SupplyRequest("LAB", 0, new String[]{"PEN"}, new int[]{1000}), "units");
        String[] maximum = new String[64];
        java.util.Arrays.fill(maximum, "PEN");
        int[] quantities = new int[64];
        java.util.Arrays.fill(quantities, 999);
        same(64, new SupplyRequest("LAB", 2, maximum, quantities).itemCodes().length, "64 rows");
        invalid(() -> new SupplyRequest("LAB", 0, new String[65], new int[65]), "items");
        for (String malformed : new String[] {
            "", "LAB|1", "LAB|1||", "|1|", "lab|1|", "LAB|3|", "LAB|01|", "LAB|+1|",
            "LAB|1|PEN:0", "LAB|1|PEN:01", "LAB|1|PEN:1000", "LAB|1|PEN:-1",
            "LAB|1|PEN:1;", "LAB|1|:1", "LAB|1|PEN:1:2", "LAB|1|PEN", "LAB|1| PEN:1"
        }) {
            invalid(() -> LegacySupplyRequest.parse(malformed), "row");
            invalid(() -> RequestCodec.decode(malformed), "row");
        }
        invalid(() -> LegacySupplyRequest.parse(null), "row");
        invalid(() -> RequestCodec.decode(null), "row");
        invalid(() -> RequestCodec.encode(null), "request");
        same("LAB|0|", RequestCodec.decode("LAB|0|").toLegacy(), "empty body");
        SupplyRequest first = RequestCodec.decode("LAB|1|PEN:2;PAD:1");
        check(!first.equals(RequestCodec.decode("LAB|1|PAD:1;PEN:2")), "order is part of value");
        check(!first.equals(null), "null equality");
        check(!first.equals("request"), "other type equality");
        check(!first.equals(RequestCodec.decode("LAB|2|PEN:2;PAD:1")), "service participates");
        check(!RequestCodec.decode("LAB|1|PEN:2;PEN:3").equals(RequestCodec.decode("LAB|1|PEN:5")), "duplicates retained");
    }
    private static void core() {
        invalid(() -> new ItemCode(null), "code");
        invalid(() -> new ItemCode(""), "code");
        invalid(() -> new ItemCode("lower"), "code");
        invalid(() -> new ItemCode("TOOLONG99"), "code");
        invalid(() -> new RequestLine(null, 0), "code");
        invalid(() -> new RequestLine(new ItemCode("PEN"), 0), "units");
        same(new ItemCode("PEN"), new ItemCode(new String("PEN")), "code value equality");
        check(new ItemCode("PEN").hashCode() == new ItemCode("PEN").hashCode(), "code hash");
        for (int code = 0; code <= 2; code++) {
            same(code, ServiceLevel.fromCode(code).code(), "enum code");
            same(new int[]{180, 60, 30}[code], ServiceLevel.fromCode(code).dispatchMinutes(), "enum rule");
        }
        invalid(() -> ServiceLevel.fromCode(3), "service");
        ArrayList<RequestLine> input = new ArrayList<>(List.of(new RequestLine(new ItemCode("PEN"), 2)));
        SupplyRequest request = SupplyRequest.fromLines("LAB", ServiceLevel.RUSH, input);
        int stableHash = request.hashCode();
        HashMap<SupplyRequest, String> map = new HashMap<>();
        map.put(request, "saved");
        input.add(new RequestLine(new ItemCode("PAD"), 1));
        same(1, request.lines().size(), "snapshot versus live view");
        same(stableHash, request.hashCode(), "stable map key");
        same("saved", map.get(RequestCodec.decode("LAB|1|PEN:2")), "equal map key");
        immutable(() -> request.lines().add(new RequestLine(new ItemCode("MUG"), 1)));
        immutable(() -> request.lines().set(0, new RequestLine(new ItemCode("MUG"), 1)));
        same("LAB|1|PEN:2", request.toLegacy(), "unmodified after rejected writes");
        invalid(() -> SupplyRequest.fromLines("LAB", null, List.of()), "service");
        invalid(() -> SupplyRequest.fromLines("LAB", ServiceLevel.RUSH, null), "lines");
        ArrayList<RequestLine> nullLine = new ArrayList<>();
        nullLine.add(null);
        invalid(() -> SupplyRequest.fromLines("LAB", ServiceLevel.RUSH, nullLine), "lines");
        List<RequestLine> tooMany = new ArrayList<>();
        for (int i = 0; i < 65; i++) tooMany.add(new RequestLine(new ItemCode("PEN"), 1));
        invalid(() -> SupplyRequest.fromLines("LAB", ServiceLevel.RUSH, tooMany), "lines");
        for (String row : List.of("LAB|0|", "LAB|1|PEN:1", "OFFICE|2|PAD:999;PEN:2;PAD:1")) {
            SupplyRequest roundTrip = RequestCodec.decode(row);
            same(row, RequestCodec.encode(roundTrip), "round-trip bytes");
            check(roundTrip.equals(SupplyRequest.fromLines(roundTrip.destination(),
                ServiceLevel.fromCode(roundTrip.serviceCode()), roundTrip.lines())), "typed constructor equality");
        }
    }
    private static void extension() {
        SupplyRequest original = RequestCodec.decode("LAB|1|PEN:2;PAD:1;PEN:3");
        same("LAB|1|PEN:5;PAD:1", original.merged().toLegacy(), "first occurrence order");
        same("LAB|1|PEN:2;PAD:1;PEN:3", original.toLegacy(), "extension preserves original");
        same(original.merged(), original.merged().merged(), "merge idempotence");
        same("LAB|0|", RequestCodec.decode("LAB|0|").merged().toLegacy(), "empty merge");
        invalid(() -> RequestCodec.decode("LAB|0|PEN:999;PEN:1").merged(), "units");
    }
    public static void main(String[] args) {
        String mode = args.length == 0 ? "all" : args[0];
        if (!List.of("base", "core", "all").contains(mode)) throw new IllegalArgumentException("mode");
        baseline();
        if (!mode.equals("base")) core();
        if (mode.equals("all")) extension();
        System.out.println(mode + " data contracts: " + checks + " passed");
    }
}
