import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    System.out.println("\nHello, what's your name?");
    String name = in.nextLine();

    //First Question
    System.out.println("It's nice to meet you " + name + "! What's your favorite thing to do in your free time?");
    String activity = in.nextLine();

    //Second Question
    System.out.println("Wow, " + activity + " seems fun; I'll have to try it out. What's your favorite food?");
    String food = in.nextLine();

    //Third Question
    System.out.println("I'll have to try " + food + " sometime. Do you have a good book I could read any time soon?");
    String book = in.nextLine();

    System.out.println("Awesome! " + book + " sounds like a great read. I'll see you later!");
  }
}