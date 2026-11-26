import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    record Student(String name, int gpaHundredths) {}

    static List<Student> rank(List<Student> students) {
        var order = Comparator.comparingInt(Student::gpaHundredths).reversed()
                .thenComparing(Student::name);
        var queue = new PriorityQueue<>(order);
        queue.addAll(students);
        var ranking = new ArrayList<Student>(students.size());
        while (!queue.isEmpty()) {
            ranking.add(queue.remove());
        }
        return ranking;
    }

    static List<Student> topWithTies(List<Student> students, int limit) {
        if (limit <= 0) return List.of();

        var ranking = rank(students);
        if (ranking.size() <= limit) return ranking;

        int cutoff = ranking.get(limit - 1).gpaHundredths();
        int end = limit;
        while (end < ranking.size() && ranking.get(end).gpaHundredths() == cutoff) {
            end++;
        }
        return List.copyOf(ranking.subList(0, end));
    }

    public static void main(String[] args) {
        var students = List.of(new Student("Zoe", 390), new Student("Mei", 400),
                new Student("Ana", 390), new Student("Ray", 355));
        var ranking = args.length == 1 && args[0].equals("cutoff")
                ? topWithTies(students, 2) : rank(students);
        for (var student : ranking) {
            System.out.printf(java.util.Locale.ROOT, "%s: %d.%02d%n",
                    student.name(), student.gpaHundredths() / 100, student.gpaHundredths() % 100);
        }
    }
}
