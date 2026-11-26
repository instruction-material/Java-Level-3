public class Main {
    static final class Node<T> {
        private T value;

        Node(T initial) {
            value = initial;
        }

        T get() {
            // TODO: Return a value with its node type.
            throw new UnsupportedOperationException("Return a value with its node type");
        }

        void set(T replacement) {
            // TODO: Update the typed value.
            throw new UnsupportedOperationException("Update the typed value");
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    static Node<?>[] mixed() {
        // TODO: Assemble an array of nodes with independent value types.
        throw new UnsupportedOperationException("Assemble an array of nodes with independent value types");
    }

    public static void main(String[] args) {
        for (Node<?> value : mixed()) System.out.println(value);
    }
}
