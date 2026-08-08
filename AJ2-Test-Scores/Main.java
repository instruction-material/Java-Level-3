import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        //get the user name and class
        System.out.print("\nEnter your name: ");
        String name = in.nextLine();
        System.out.print("Enter your class name: ");
        String className = in.nextLine();

        //get the number of tests
        System.out.print("How many tests have you taken? ");
        int numTests = in.nextInt();

        //get all the test scores and save them in an array
        int[] scores = new int[numTests];
        for (int i = 0; i < numTests; i++) {
            System.out.print("Enter you score for test #" +
                             Integer.toString(i + 1) + ": ");
            scores[i] = in.nextInt();
        }

        //use the average() method and letterGrade() method to print the current grade in the class
        double average = average(scores);
        System.out.println("Your grade in " + className + " is a " +
                           letterGrade(average) + ".");
    }

    public static double average(int[] scores) {
        int sum = 0;
        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
        }
        return sum / scores.length;
    }

    public static String letterGrade(double average) {

        //optional rounding feature
        double wholeNum = (double)(Math.floor(average));
        if (Math.abs(average - wholeNum) >= 0.5) {
            average = wholeNum + 1;
        }

        if (average >= 90) {
            return "A";
        } else if (average >= 80 && average < 90) {
            return "B";
        } else if (average >= 70 && average < 80) {
            return "C";
        } else if (average >= 60 && average < 70) {
            return "D";
        }
        return "F";
    }
}
