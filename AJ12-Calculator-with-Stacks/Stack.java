public class Stack {

    static final int MAX = 1000;
    private int top;
    private String s[];

    public Stack() {
        s = new String[MAX];
        top = 0;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    public void push(String x) {
        if (top == s.length) {
            System.out.println("Stack Overflow");
        } else {
            s[top++] = x;
        }
    }

    public String pop() {
        if (top <= 0) {
            System.out.println("Stack Underflow");
            throw new EmptyException();
        } else {
            String x = s[--top];
            return x;
        }
    }

    public String peek() {
        if (top <= 0) {
            System.out.println("Stack Underflow");
            throw new EmptyException();
        } else {
            String x = s[top - 1];
            return x;
        }
    }

    public String toString() {
        var values = new java.util.StringJoiner(" ", "[", "]");
        for (int i = 0; i < top; i++) {
            values.add(s[i]);
        }
        return values.toString();
    }
}
