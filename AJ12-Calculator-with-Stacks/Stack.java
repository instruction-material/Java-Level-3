public class Stack {

    static final int MAX = 1000;
    private int top;
    private String s[];

    public Stack() {
        s = new String[MAX];
        top = 0;
    }

    public boolean isEmpty() {
        return (top <= 0);
    }

    public void push(String x) {
        if (top >= (MAX - 1)) {
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
        String str = "";
        for (int i = 0; i < top; i++) {
            if (i == 0) {
                str += "[";
            }
            if (i != top - 1) {
                str += s[i] + " ";
            }
            if (i == top - 1) {
                str += s[i] + "]";
            }
        }
        return str;
    }
}
