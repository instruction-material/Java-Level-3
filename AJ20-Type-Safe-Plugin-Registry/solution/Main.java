import java.util.*;

class Main {
    interface Plugin<T> {
        String apply(T input);
    }

    static class Registry<T> {
        private final Map<String, Plugin<T>> byName = new LinkedHashMap<>();

        void register(String name, Plugin<T> plugin) {
            String key = name.strip();
            if (key.isEmpty() || byName.putIfAbsent(key, Objects.requireNonNull(plugin)) != null) {
                throw new IllegalArgumentException("Blank or duplicate command");
            }
        }

        String run(String name, T input) {
            Plugin<T> plugin = byName.get(name.strip());
            if (plugin == null) throw new IllegalArgumentException("Unknown command");
            return plugin.apply(input);
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
