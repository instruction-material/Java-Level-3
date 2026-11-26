import java.util.Comparator;

public class StudentComparator implements Comparator<Student> {
    @Override
    public int compare(Student first, Student second) {
        int byGpa = Double.compare(second.getGPA(), first.getGPA());
        return byGpa != 0 ? byGpa : first.getName().compareTo(second.getName());
    }
}
