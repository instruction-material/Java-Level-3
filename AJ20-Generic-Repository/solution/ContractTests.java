import java.util.*;
import java.nio.file.*;
import java.time.*;

public class ContractTests {
    static void same(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    static void fails(Class<? extends RuntimeException> type, Runnable change) {
        try {
            change.run();
        } catch (RuntimeException error) {
            if (type.isInstance(error)) return;
            throw error;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    public static void main(String[] args) throws Exception {
        var courses = new Repository<CourseRecord>();
        var java = new CourseRecord("j", "Java", 22, true);
        var cpp = new CourseRecord("c", "C++", 10, true);
        var old = new CourseRecord("o", "Archive", 20, false);
        courses.add(java);
        courses.add(cpp);
        courses.add(old);
        same(List.of(java, cpp), courses.findAll(row -> row.active() && row.isLargeCourse()));
        same(List.of(cpp, old, java), courses.sorted(Comparator.comparingInt(CourseRecord::moduleCount)));
        same(List.of(java, cpp, old), courses.all());
        var snapshot = courses.findAll(CourseRecord::active);
        fails(UnsupportedOperationException.class, () -> snapshot.removeFirst());
        same(Optional.of(cpp), courses.remove("c"));
        same(false, courses.contains("c"));
        same(List.of(java, cpp), snapshot);
        same(Optional.empty(), courses.get("missing"));
        fails(IllegalArgumentException.class, () -> courses.add(java));
        fails(IllegalArgumentException.class, () -> new CourseRecord(" ", "Blank", 1, true));
        var students = new Repository<StudentRecord>();
        var ada = new StudentRecord("a", "Ada", 12);
        students.add(ada);
        same(List.of(ada), students.findAll(person -> person.completedModules() >= 12));
        same(ada.hashCode(), new StudentRecord("a", "Ada", 12).hashCode());
        System.out.println("Engineering checks passed");
    }
}
