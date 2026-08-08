class Main {
    public static void main(String[] args) {

        Stack s = new Stack();
        //s.pop();
        s.push(10);
        s.push(20);
        s.push(30);

        System.out.println("\nStack: " + s);
        System.out.println("Peek: " + s.peek() + "\n");

        for (int i = 0; i < 3; i++) {
            System.out.println("Stack: " + s);
            s.pop();
        }
        System.out.println("Stack: " + s);
    }
}
