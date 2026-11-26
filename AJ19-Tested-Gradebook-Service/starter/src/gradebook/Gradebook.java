package gradebook;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public final class Gradebook {
    public record Student(String id, String name) {}
    public record Assignment(String id, int weight) {}
    private record ScoreKey(String student, String assignment) {}

    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, Assignment> assignments = new HashMap<>();
    private final Map<ScoreKey, Double> scores = new HashMap<>();

    public void addStudent(Student student) {
        if (student.id().isBlank() || student.name().isBlank()
                || students.putIfAbsent(student.id(), student) != null) {
            throw new IllegalArgumentException("Expected a new student with a name and ID");
        }
    }

    public void addAssignment(Assignment assignment) {
        if (assignment.id().isBlank() || assignment.weight() <= 0
                || assignments.putIfAbsent(assignment.id(), assignment) != null) {
            throw new IllegalArgumentException("Expected a new assignment with a positive weight");
        }
    }

    public void submit(String student, String assignment, double score) {
        throw new UnsupportedOperationException("Validate and store a score");
    }

    public OptionalDouble mean(String student) {
        throw new UnsupportedOperationException("Compute the weighted mean of submitted scores");
    }

    public static String grade(double mean) {
        if (mean >= 90) return "A";
        if (mean >= 80) return "B";
        if (mean >= 70) return "C";
        if (mean >= 60) return "D";
        return "F";
    }
}
