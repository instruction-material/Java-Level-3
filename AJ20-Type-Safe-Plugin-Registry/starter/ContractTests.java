import java.util.*;
import java.nio.file.*;
import java.time.*;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void fails(Class<? extends RuntimeException> type, Runnable change) {
        try {
            change.run();
        } catch (RuntimeException error) {
            if (type.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        var words = Main.textPlugins();
        same("LEAF", words.run(" upper ", "leaf"));
        same("fael", words.run("reverse", "leaf"));
        same("leaf", words.run("trim", " leaf "));
        same("4", words.run("length", "leaf"));
        fails(IllegalArgumentException.class, () -> words.register(" trim ", String::strip));
        fails(IllegalArgumentException.class, () -> words.run("unknown", "leaf"));
        fails(IllegalArgumentException.class, () -> words.register(" ", String::strip));
        var numbers = new Main.Registry<Integer>();
        numbers.register("double", number -> Long.toString(2L * number));
        same("4294967294", numbers.run("double", Integer.MAX_VALUE));
        System.out.println("Engineering checks passed");
    }
}
