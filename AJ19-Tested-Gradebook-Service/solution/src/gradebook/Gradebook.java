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
        if (!students.containsKey(student) || !assignments.containsKey(assignment)) {
            throw new IllegalArgumentException("Unknown student or assignment");
        }
        if (!Double.isFinite(score) || score < 0 || score > 100) {
            throw new IllegalArgumentException("Score outside 0 through 100");
        }
        scores.put(new ScoreKey(student, assignment), score);
    }

    public OptionalDouble mean(String student) {
        if (!students.containsKey(student)) throw new IllegalArgumentException("Unknown student");
        double weighted = 0;
        long weight = 0;
        for (Assignment assignment : assignments.values()) {
            Double score = scores.get(new ScoreKey(student, assignment.id()));
            if (score != null) {
                weighted += score * assignment.weight();
                weight += assignment.weight();
            }
        }
        return weight == 0 ? OptionalDouble.empty() : OptionalDouble.of(weighted / weight);
    }

    public static String grade(double mean) {
        if (mean >= 90) return "A";
        if (mean >= 80) return "B";
        if (mean >= 70) return "C";
        if (mean >= 60) return "D";
        return "F";
    }
}
