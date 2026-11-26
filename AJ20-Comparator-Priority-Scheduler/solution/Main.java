import java.util.*;

class Main {
    record Task(String id, int deadline, int importance, int duration) {
        Task {
            if (id.isBlank() || deadline < 0 || importance < 0 || duration <= 0) {
                throw new IllegalArgumentException("Invalid task");
            }
        }
    }

    enum Policy { DEADLINE, IMPORTANCE, DURATION }

    static Comparator<Task> comparator(Policy policy) {
        return switch (policy) {
            case DEADLINE -> Comparator.comparingInt(Task::deadline);
            case IMPORTANCE -> Comparator.comparingInt(Task::importance).reversed();
            case DURATION -> Comparator.comparingInt(Task::duration);
        };
    }

    static List<String> fromQueue(List<Task> tasks, Comparator<Task> primary) {
        var queue = new PriorityQueue<>(primary.thenComparing(Task::id));
        for (Task task : tasks) queue.offer(task);
        var order = new ArrayList<String>();
        while (!queue.isEmpty()) order.add(queue.remove().id());
        return List.copyOf(order);
    }

    static List<String> fromSortedList(List<Task> tasks, Comparator<Task> primary) {
        return tasks.stream().sorted(primary.thenComparing(Task::id)).map(Task::id).toList();
    }

    static List<Task> example() {
        return List.of(new Task("C", 4, 1, 1), new Task("A", 3, 1, 4),
                new Task("B", 3, 9, 2));
    }

    public static void main(String[] args) {
        for (Policy policy : Policy.values()) {
            System.out.println(policy + " " + fromQueue(example(), comparator(policy)));
        }
    }
}
