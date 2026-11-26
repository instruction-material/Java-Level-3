import java.util.*;

class Main {
    static class Student {
        private final String name;
        private final int id;
        private boolean present;
        private static int count;

        Student(String name, int id) {
            if (name.isBlank() || id <= 0) throw new IllegalArgumentException("Expected a name and a positive ID");
            this.name = name.strip();
            this.id = id;
            count++;
        }

        void markPresent(boolean value) {
            present = value;
        }

        String status() {
            return name + " #" + id + (present ? " present" : " absent");
        }

        static int countPresent(List<Student> students) {
            int total = 0;
            for (Student student : students) {
                if (student.present) total++;
            }
            return total;
        }
    }

    public static void main(String[] args) {
        var cedar = new Student("Cedar", 7);
        var rowan = new Student("Rowan", 9);
        cedar.markPresent(true);
        System.out.println(cedar.status());
        System.out.println(rowan.status());
        System.out.println(Student.count);
        if (args.length > 0) System.out.println(Student.countPresent(List.of(cedar, rowan)));
    }
}
