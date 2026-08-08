public class Stack {

    static final int MAX = 1000;
    private int top;
    private char s[];

    public Stack() {
        s = new char[MAX];
        top = 0;
    }

    public boolean isEmpty() {
        return (top <= 0);
    }

    public void push(char x) {
        if (top >= (MAX - 1)) {
            System.out.println("Stack Overflow");
        } else {
            s[top++] = x;
        }
    }

    public char pop() {
        if (top <= 0) {
            System.out.println("Stack Underflow");
            return 0;
        } else {
            char x = s[--top];
            return x;
        }
    }

    public char peek() {
        if (top <= 0) {
            System.out.println("Stack Underflow");
            return 0;
        } else {
            char x = s[top - 1];
            return x;
        }
    }

    public void printStack() {
        for (int i = 0; i < top; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.println();
    }
}
