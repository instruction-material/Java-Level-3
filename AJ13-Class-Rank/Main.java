import java.util.*;

import java.util.*;
import java.math.*;

class Main {
    public static void main(String[] args) {
        PriorityQueue<Student> pq =
            new PriorityQueue<Student>(5, new StudentComparator());

        Random random = new Random();
        String[] names = {"James",   "John",    "Sabrina",  "Samantha",
                          "Ali",     "Ashley",  "Elise",    "Chandler",
                          "Thoshan", "Arjun",   "Sammeer",  "Kate",
                          "Carly",   "Gabriel", "Finn",     "Carla",
                          "Melissa", "Eliza",   "Angelica", "Peggy"};

        for (int i = 0; i < 20; i++) {
            double decimal =
                (double)Math.round(random.nextDouble() * 100) / 100;
            double gpa = 3.0 + decimal;
            Student student = new Student(names[i], gpa);
            pq.add(student);
        }

        //print students in order of priority
        System.out.println("\nClass Rank:\n");
        while (!pq.isEmpty()) {
            Student student = pq.poll();
            System.out.printf("%s: %.2f \n", student.getName(),
                              student.getGPA());
        }
    }
}
