import java.util.*;

//OUTER CLASS
public class Classroom {
    private String roomNumber;
    private ArrayList<Student> students = new ArrayList<>();

    //STATIC NESTED CLASS ---------------------------------------------
    public static final class Student {
        private String name;
        private int testScore;

        //a static nested class does NOT have access to the outer class variables and methods unless they are static, or it has a reference to an outer class object
        //that's why we save a reference to an outer class object here:
        private Classroom classroom;

        public Student(String name, int testScore) {
            this.name = name;
            this.testScore = testScore;
        }
        private void setClassroom(Classroom classroom) {
            this.classroom = classroom;
        }
        public String toString() {
            String result = "";
            result += name + ", ";
            result += Integer.toString(testScore) + ", ";
            if (classroom != null) {
                result += classroom.getRoomNumber();
            } else {
                result += "NONE";
            }
            return result;
        }
    }
    //-----------------------------------------------------------------

    public Classroom(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void addStudent(Student student) {
        students.add(student);
        student.setClassroom(this);
    }
    public String toString() {
        //The outer class has complete access to static nested class variables and methods, even if declared private
        String result = "";
        result += "\n" + roomNumber;
        result += "\nStudents:";
        for (Student student : students) {
            result += "\n\t" + student;
        }
        return result;
    }
}
