public class Main {
    static final class Node<T> {
        private T value;

        Node(T initial) {
            value = initial;
        }

        T get() {
            return value;
        }

        void set(T replacement) {
            value = replacement;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    static Node<?>[] mixed() {
        Node<Character> letter = new Node<>('A');
        letter.set('C');
        Node<String> greeting = new Node<>("hello");
        greeting.set(greeting.get() + " world");
        return new Node<?>[]{letter, greeting, new Node<>(true), new Node<>(100), new Node<>(3.1415)};
    }

    public static void main(String[] args) {
        for (Node<?> value : mixed()) System.out.println(value);
    }
}
