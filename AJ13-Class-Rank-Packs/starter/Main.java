import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    record Student(String name, int gpaHundredths) {}

    static List<Student> rank(List<Student> students) {
        // TODO: Rank by descending GPA and ascending name.
        throw new UnsupportedOperationException("Rank by descending GPA and ascending name");
    }

    static List<Student> topWithTies(List<Student> students, int limit) {
        // TODO: Include everyone tied at the cutoff.
        throw new UnsupportedOperationException("Include everyone tied at the cutoff");
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
