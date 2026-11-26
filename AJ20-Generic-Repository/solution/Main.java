import java.util.Comparator;
import java.util.List;

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
