package commands;

import java.util.Locale;

public final class Parser {
    public sealed interface Command permits Add, Print, Quit {}
    public record Add(String item, int count) implements Command {}
    public record Print() implements Command {}
    public record Quit() implements Command {}

    public Command parse(String line) {
        String[] fields = line.strip().split("\\s+");
        String verb = fields[0].toUpperCase(Locale.ROOT);
        if (verb.equals("ADD") && fields.length == 3) {
            int count = Integer.parseInt(fields[2]);
            if (count > 0) return new Add(fields[1], count);
        }
        if (verb.equals("PRINT") && fields.length == 1) return new Print();
        if (verb.equals("QUIT") && fields.length == 1) return new Quit();
        throw new IllegalArgumentException("Expected ADD name count, PRINT or QUIT");
    }
}
