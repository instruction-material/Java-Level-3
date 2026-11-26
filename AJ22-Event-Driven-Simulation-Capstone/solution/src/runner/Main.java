package runner;
import input.Script;
import service.Circulation;
import service.EventSink;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void process(Script.Input input, EventSink output) throws IOException {
        var service = new Circulation(input.inventory());
        for (var event : input.events()) output.write(service.apply(event));
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 2) throw new IllegalArgumentException("Expected [script] [new report path]");
        var source = Path.of(args.length == 0 ? "requests.txt" : args[0]);
        var input = Script.load(source);
        if (args.length < 2) {
            process(input, System.out::println);
        } else {
            try (var file = Files.newBufferedWriter(Path.of(args[1]), StandardOpenOption.CREATE_NEW)) {
                process(input, line -> {
                    file.write(line);
                    file.newLine();
                });
            }
        }
    }
}
