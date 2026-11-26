package commands;

import java.util.Locale;

public final class Parser {
    public sealed interface Command permits Add, Print, Quit {}
    public record Add(String item, int count) implements Command {}
    public record Print() implements Command {}
    public record Quit() implements Command {}

    public Command parse(String line) {
        throw new UnsupportedOperationException("Parse a full command into a typed value");
    }
}
