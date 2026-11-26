import java.util.*;

class Main {
    interface Plugin<T> {
        String apply(T input);
    }

    static class Registry<T> {
        private final Map<String, Plugin<T>> byName = new LinkedHashMap<>();

        void register(String name, Plugin<T> plugin) {
            // TODO: Register one typed command without replacing an existing name.
            throw new UnsupportedOperationException("Register one typed command without replacing an existing name");
        }

        String run(String name, T input) {
            // TODO: Resolve and call the named typed command.
            throw new UnsupportedOperationException("Resolve and call the named typed command");
        }
    }

    static Registry<String> textPlugins() {
        var registry = new Registry<String>();
        registry.register("upper", text -> text.toUpperCase(Locale.ROOT));
        registry.register("reverse", text -> new StringBuilder(text).reverse().toString());
        registry.register("trim", String::strip);
        registry.register("length", text -> Integer.toString(text.length()));
        return registry;
    }

    public static void main(String[] args) {
        var text = textPlugins();
        System.out.println(text.run("upper", "leaf"));
        System.out.println(text.run("reverse", "leaf"));
        System.out.println(text.run("trim", " leaf "));
        System.out.println(text.run("length", "leaf"));
    }
}
