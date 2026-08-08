class Main {
    public static void main(String[] args) {
        Queue q = new Queue(5);

        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        System.out.println("\n" + q);
        System.out.println("Front element is: " + q.front());
        System.out.println("Queue size is " + q.size() + "\n");

        q.dequeue();
        System.out.println(q);
        System.out.println("Front element is: " + q.front());
        System.out.println("Queue size is " + q.size() + "\n");

        q.dequeue();
        System.out.println(q);
        System.out.println("Front element is: " + q.front());
        System.out.println("Queue size is " + q.size() + "\n");

        q.dequeue();
        if (q.isEmpty()) {
            System.out.println("Queue Is Empty");
        } else {
            System.out.println("Queue Is Not Empty");
        }

        // q.enqueue(1);
        // q.enqueue(2);
        // q.enqueue(3);
        // System.out.println("\n" + q);
    }
}
