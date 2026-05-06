import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

interface Identified {
    String id();
}

record CourseRecord(String id, String title, int moduleCount, boolean active) implements Identified {
    public CourseRecord {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (moduleCount < 0) {
            throw new IllegalArgumentException("module count cannot be negative");
        }
    }

    public boolean isLargeCourse() {
        return moduleCount >= 10;
    }
}

record StudentRecord(String id, String name, int completedModules) implements Identified {
    public StudentRecord {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (completedModules < 0) {
            throw new IllegalArgumentException("completed modules cannot be negative");
        }
    }
}

class Repository<T extends Identified> {
    private final Map<String, T> items = new LinkedHashMap<>();

    public void add(T item) {
        if (items.containsKey(item.id())) {
            throw new IllegalArgumentException("duplicate id: " + item.id());
        }
        items.put(item.id(), item);
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public boolean contains(String id) {
        return items.containsKey(id);
    }

    public Optional<T> remove(String id) {
        return Optional.ofNullable(items.remove(id));
    }

    public List<T> all() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    public List<T> findAll(Predicate<T> filter) {
        // TODO: Return a safe list containing only items that match the filter.
        return List.of();
    }
}

public class Main {
    public static void main(String[] args) {
        Repository<CourseRecord> courses = new Repository<>();
        courses.add(new CourseRecord("java-3", "Java Level 3", 22, true));
        courses.add(new CourseRecord("cpp-3", "C++ Level 3", 8, true));
        courses.add(new CourseRecord("legacy-java", "Legacy Java Review", 4, false));

        Repository<StudentRecord> students = new Repository<>();
        students.add(new StudentRecord("s-100", "Ada", 18));
        students.add(new StudentRecord("s-200", "Grace", 11));

        CourseRecord java3 = courses.get("java-3").orElseThrow();
        System.out.println(java3.title());
        System.out.println(java3);

        // TODO: Print all active large courses.
        // TODO: Print all students with at least 12 completed modules.
        // TODO: Try to add a duplicate ID and handle the exception cleanly.
    }
}
