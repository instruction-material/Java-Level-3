import java.util.*;

public class StudentComparator implements Comparator<Student> {
    public int compare(Student first, Student second) {
        if (first.getGPA() < second.getGPA()) {
            return 1;
        } else if (first.getGPA() > second.getGPA()) {
            return -1;
        }
        return 0;
    }
}
