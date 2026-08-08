public class Stack {

    private static final int MAX = 1000;
    private int top;
    private int stack[];

    public Stack() {
        stack = new int[MAX];
        top = 0;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public void push(int data) {
        if (top < MAX) {
            stack[top++] = data;
        } else {
            System.out.println("Stack Overflow");
            throw new OverflowException();
        }
    }

    public int pop() {
        if (top > 0) {
            return stack[--top];
        } else {
            System.out.println("Stack Underflow");
            throw new UnderflowException();
        }
    }

    public int peek() {
        if (top > 0) {
            return stack[top - 1];
        } else {
            System.out.println("Stack Underflow");
            throw new UnderflowException();
        }
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < top; i++) {
            result += stack[i] + " ";
        }
        return result;
    }
}
