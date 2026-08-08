import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("\nEnter an animal: ");
        String animal = in.nextLine();

        System.out.print("Enter a verb: ");
        String verb = in.nextLine();

        System.out.print("Enter an adjective: ");
        String adj = in.nextLine();

        System.out.print("Enter a noun: ");
        String noun2 = in.nextLine();

        System.out.println("There was once a very " + adj + " " + animal +
                           " who liked to put " + noun2 +
                           " in his pasta. In his free time, he likes to " +
                           verb + ". How interesting!");
    }
}
