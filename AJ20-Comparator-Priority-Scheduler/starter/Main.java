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
        // TODO: Select and stabilize a task ordering policy.
        throw new UnsupportedOperationException("Select and stabilize a task ordering policy");
    }

    static List<String> fromQueue(List<Task> tasks, Comparator<Task> primary) {
        // TODO: Poll tasks in comparator order.
        throw new UnsupportedOperationException("Poll tasks in comparator order");
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
