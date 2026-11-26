package runner;

import gradebook.Gradebook;
import java.util.Locale;

public final class Main {
    public static void main(String[] args) {
        var book = new Gradebook();
        book.addStudent(new Gradebook.Student("S1", "Cedar"));
        book.addAssignment(new Gradebook.Assignment("practice", 1));
        book.addAssignment(new Gradebook.Assignment("project", 3));
        book.submit("S1", "practice", 80);
        book.submit("S1", "project", 90);
        double mean = book.mean("S1").orElseThrow();
        System.out.printf(Locale.ROOT, "%.2f %s%n", mean, Gradebook.grade(mean));
    }
}
