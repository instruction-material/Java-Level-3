package checks;

import java.util.Objects;
import gradebook.Gradebook;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void rejects(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected invalid input");
    }

    public static void main(String[] args) {
        var book = new Gradebook();
        book.addStudent(new Gradebook.Student("S1", "Cedar"));
        book.addStudent(new Gradebook.Student("S2", "Rowan"));
        book.addAssignment(new Gradebook.Assignment("practice", 1));
        book.addAssignment(new Gradebook.Assignment("project", 3));
        same(true, book.mean("S2").isEmpty());
        rejects(() -> book.submit("S1", "unknown", 80));
        rejects(() -> book.submit("unknown", "practice", 80));
        book.submit("S1", "practice", 80);
        same(80.0, book.mean("S1").orElseThrow());
        book.submit("S1", "project", 90);
        same(87.5, book.mean("S1").orElseThrow());
        same("B", Gradebook.grade(book.mean("S1").orElseThrow()));
        rejects(() -> book.submit("S1", "project", Double.NaN));
        rejects(() -> book.submit("S1", "project", 101));
        same(87.5, book.mean("S1").orElseThrow());
        book.submit("S1", "project", 100);
        same(95.0, book.mean("S1").orElseThrow());
        same("A", Gradebook.grade(90));
        System.out.println("AJ19 checks passed");
    }
}
