package checks;
import input.Script;
import model.Event;
import runner.Main;
import service.Circulation;
import java.nio.file.*;
import java.util.*;

public class ContractTests {
    static void same(Object wanted, Object got) {
        if (!Objects.equals(wanted, got)) throw new AssertionError(wanted + " != " + got);
    }

    public static void main(String[] args) throws Exception {
        var shelf = new Circulation(Map.of("oak",1));
        if (args.length == 1 && args[0].equals("extension")) {
            shelf.apply(new Event.Request("A","oak"));
            shelf.apply(new Event.Request("B","oak"));
            var captured = shelf.waiting("oak");
            try {
                captured.add("external");
                throw new AssertionError("Returned waiting list permits mutation");
            } catch (UnsupportedOperationException expected) {
                same(List.of("B"), shelf.waiting("oak"));
            }
            shelf.apply(new Event.Return("A","oak"));
            same(List.of("B"), captured);
            same(List.of(), shelf.waiting("oak"));
            System.out.println("Snapshot checks passed");
            return;
        }
        same("loan A oak", shelf.apply(new Event.Request("A","oak")));
        same("waiting B oak", shelf.apply(new Event.Request("B","oak")));
        same("waiting C oak", shelf.apply(new Event.Request("C","oak")));
        same("duplicate B oak", shelf.apply(new Event.Request("B","oak")));
        same("handoff A B oak", shelf.apply(new Event.Return("A","oak")));
        same(List.of("C"), shelf.waiting("oak"));
        same(0, shelf.available("oak"));
        same("cancelled C oak", shelf.apply(new Event.Cancel("C","oak")));
        same("no loan A oak", shelf.apply(new Event.Return("A","oak")));
        same("returned B oak", shelf.apply(new Event.Return("B","oak")));
        same(1, shelf.available("oak"));

        var output = new ArrayList<String>();
        Main.process(Script.load(Path.of("requests.txt")),output::add);
        same(List.of("loan Ada oak","waiting Bea oak","waiting Cy oak","handoff Ada Bea oak","cancelled Cy oak","returned Bea oak"),output);
        Path bad = Files.createTempFile("java-requests-", ".txt");
        try {
            Files.writeString(bad, "stock oak 1\nunknown A oak\n");
            try {
                Script.load(bad);
                throw new AssertionError("Expected a line-numbered parse error");
            } catch (IllegalArgumentException expected) {
                same("Line 2: Unknown event unknown", expected.getMessage());
            }
        } finally {
            Files.deleteIfExists(bad);
        }
        System.out.println("Simulation checks passed");
    }
}
