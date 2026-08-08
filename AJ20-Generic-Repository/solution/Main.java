import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

// Identify records that can be stored by a stable id
interface Identified {
    /**
     * @brief Return the stable id for this record
     *
     * @return Stable record id
     */
    String id();
}

// Store course metadata in an immutable record
record CourseRecord(String id, String title, int moduleCount, boolean active)
    implements Identified {
    private static final int MINIMUM_MODULE_COUNT = 0;
    private static final int LARGE_COURSE_MODULE_THRESHOLD = 10;

    public CourseRecord {
        // Require every course to have a usable id
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }

        // Require every course to have a readable title
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }

        // Reject impossible negative module counts
        if (moduleCount < MINIMUM_MODULE_COUNT) {
            throw new IllegalArgumentException(
                "module count cannot be negative");
        }
    }

    /**
     * @brief Check whether the course has many modules
     *
     * @return True when the course meets the large-course threshold
     */
    public boolean isLargeCourse() {
        return moduleCount >= LARGE_COURSE_MODULE_THRESHOLD;
    }
}

// Store student progress in an immutable record
record StudentRecord(String id, String name, int completedModules)
    implements Identified {
    private static final int MINIMUM_COMPLETED_MODULES = 0;

    public StudentRecord {
        // Require every student to have a usable id
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }

        // Require every student to have a readable name
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        // Reject impossible negative progress counts
        if (completedModules < MINIMUM_COMPLETED_MODULES) {
            throw new IllegalArgumentException(
                "completed modules cannot be negative");
        }
    }
}

// Store and query items by their stable id
class Repository<T extends Identified> {
    private final Map<String, T> items = new LinkedHashMap<>();

    /**
     * @brief Add one item to the repository
     *
     * @param item Item to store
     */
    public void add(T item) {
        // Protect the repository from duplicate ids
        if (items.containsKey(item.id())) {
            throw new IllegalArgumentException("duplicate id: " + item.id());
        }

        items.put(item.id(), item);
    }

    /**
     * @brief Get one item by id
     *
     * @param id Item id to find
     *
     * @return Optional item for the provided id
     */
    public Optional<T> get(String id) {
        return Optional.ofNullable(items.get(id));
    }

    /**
     * @brief Check whether an id is stored
     *
     * @param id Item id to check
     *
     * @return True when the repository contains the id
     */
    public boolean contains(String id) {
        return items.containsKey(id);
    }

    /**
     * @brief Remove one item by id
     *
     * @param id Item id to remove
     *
     * @return Optional removed item
     */
    public Optional<T> remove(String id) {
        return Optional.ofNullable(items.remove(id));
    }

    /**
     * @brief Return all stored items
     *
     * @return Immutable list of stored items
     */
    public List<T> all() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    /**
     * @brief Find all items that match a predicate
     *
     * @param filter Predicate used to test each item
     *
     * @return Immutable list of matching items
     */
    public List<T> findAll(Predicate<T> filter) {
        List<T> matches = new ArrayList<>();

        // Test every stored item with the caller-provided predicate
        for (T item : items.values()) {
            // Keep only items that satisfy the predicate
            if (filter.test(item)) {
                matches.add(item);
            }
        }

        return Collections.unmodifiableList(matches);
    }

    /**
     * @brief Return all items sorted with a comparator
     *
     * @param comparator Comparator that defines the item order
     *
     * @return Immutable sorted list of items
     */
    public List<T> sorted(Comparator<T> comparator) {
        List<T> sortedItems = new ArrayList<>(items.values());
        sortedItems.sort(comparator);
        return Collections.unmodifiableList(sortedItems);
    }
}

public class Main {
    private static final String JAVA_LEVEL_3_ID = "java-3";
    private static final String CPP_LEVEL_3_ID = "cpp-3";
    private static final String LEGACY_JAVA_ID = "legacy-java";
    private static final String ADA_ID = "s-100";
    private static final String GRACE_ID = "s-200";
    private static final int JAVA_LEVEL_3_MODULE_COUNT = 22;
    private static final int CPP_LEVEL_3_MODULE_COUNT = 8;
    private static final int LEGACY_JAVA_MODULE_COUNT = 4;
    private static final int ADA_COMPLETED_MODULES = 18;
    private static final int GRACE_COMPLETED_MODULES = 11;
    private static final int EXPERIENCED_MODULE_THRESHOLD = 12;
    private static final int DUPLICATE_MODULE_COUNT = 1;

    // Build the sample course repository used by the demo
    private static Repository<CourseRecord> buildCourseRepository() {
        Repository<CourseRecord> courses = new Repository<>();
        courses.add(new CourseRecord(JAVA_LEVEL_3_ID, "Java Level 3",
                                     JAVA_LEVEL_3_MODULE_COUNT, true));
        courses.add(new CourseRecord(CPP_LEVEL_3_ID, "C++ Level 3",
                                     CPP_LEVEL_3_MODULE_COUNT, true));
        courses.add(new CourseRecord(LEGACY_JAVA_ID, "Legacy Java Review",
                                     LEGACY_JAVA_MODULE_COUNT, false));
        return courses;
    }

    // Build the sample student repository used by the demo
    private static Repository<StudentRecord> buildStudentRepository() {
        Repository<StudentRecord> students = new Repository<>();
        students.add(new StudentRecord(ADA_ID, "Ada", ADA_COMPLETED_MODULES));
        students.add(
            new StudentRecord(GRACE_ID, "Grace", GRACE_COMPLETED_MODULES));
        return students;
    }

    // Print the record behavior that records provide automatically
    private static void printRecordExamples(Repository<CourseRecord> courses) {
        CourseRecord javaLevel3 = courses.get(JAVA_LEVEL_3_ID).orElseThrow();
        System.out.println(javaLevel3.title());
        System.out.println(javaLevel3);
        System.out.println(javaLevel3.equals(new CourseRecord(
            JAVA_LEVEL_3_ID, "Java Level 3", JAVA_LEVEL_3_MODULE_COUNT, true)));
    }

    // Print query examples for courses and students
    private static void printQueryExamples(Repository<CourseRecord> courses,
                                           Repository<StudentRecord> students) {
        List<CourseRecord> activeLargeCourses = courses.findAll(
            course -> course.active() && course.isLargeCourse());
        System.out.println("Active large courses: " + activeLargeCourses);

        List<StudentRecord> experiencedStudents = students.findAll(
            student
            -> student.completedModules() >= EXPERIENCED_MODULE_THRESHOLD);
        System.out.println("Experienced students: " + experiencedStudents);

        List<CourseRecord> byModuleCount = courses.sorted(
            Comparator.comparingInt(CourseRecord::moduleCount).reversed());
        System.out.println("Courses by module count: " + byModuleCount);
    }

    // Demonstrate duplicate-id protection in the repository
    private static void
    printDuplicateIdExample(Repository<CourseRecord> courses) {
        try {
            courses.add(new CourseRecord(JAVA_LEVEL_3_ID, "Duplicate Java",
                                         DUPLICATE_MODULE_COUNT, true));
        } catch (IllegalArgumentException exception) {
            System.out.println("Rejected duplicate: " + exception.getMessage());
        }
    }

    /**
     * @brief Run the generic repository demo
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Repository<CourseRecord> courses = buildCourseRepository();
        Repository<StudentRecord> students = buildStudentRepository();

        printRecordExamples(courses);
        printQueryExamples(courses, students);
        printDuplicateIdExample(courses);
    }
}
