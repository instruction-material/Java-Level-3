import java.util.*;
class Main {
    public static void main(String[] args) {

        //STATIC NESTED CLASSES
        //Notice that you can instantiate static nested classes without ever instantiating the outer class
        ArrayList<Classroom.Student> students = new ArrayList<>();
        students.add(new Classroom.Student("Billy", 87));
        students.add(new Classroom.Student("Jenny", 85));
        students.add(new Classroom.Student("Thomas", 95));
        students.add(new Classroom.Student("Rebecca", 99));
        System.out.println();
        for (Classroom.Student student : students) {
            System.out.println(student);
        }

        Classroom class1 = new Classroom("A113");
        for (Classroom.Student student : students) {
            class1.addStudent(student);
        }
        System.out.println(class1);

        //INNER CLASS
        //Notice that it's impossible to instantiate inner classes without first instantiating the outer class
        Car myCar = new Car("Toyota", "Corolla");
        System.out.println(myCar);
    }
}
