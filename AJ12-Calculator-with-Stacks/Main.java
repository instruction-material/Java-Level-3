import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        Scanner keys = new Scanner(System.in);
        Stack calc = new Stack();

        System.out.println("\nWelcome to the Stack Calculator!\n");
        System.out.println("“?” - display all numbers on the stack");
        System.out.println("“.” - pop the last number");
        System.out.println("“!” - end the program");
        System.out.println("“+” - add the last two numbers");
        System.out.println("“-” - subtract the last two numbers");
        System.out.println("“*” - multiply the last two numbers");
        System.out.println("“/” - divide the last two numbers");
        System.out.println("“%” - mod the last two numbers\n");

        String line;
        do {
            System.out.print("Enter a number or operator: ");
            line = keys.nextLine();

            String[] chars = line.split(" ");

            for (String c : chars) {
                int var1 = 0;
                int var2 = 0;

                switch (c) {
                case "?":
                    System.out.println(calc.toString());
                    break;
                case ".":
                    popNum(calc);
                    break;
                case "!":
                    System.exit(0);
                    keys.close();
                    break;
                case "+":
                    add(calc, var1, var2);
                    break;
                case "-":
                    subtract(calc, var1, var2);
                    break;
                case "*":
                    multiply(calc, var1, var2);
                    break;
                case "%":
                    mod(calc, var1, var2);
                    break;
                case "/":
                    div(calc, var1, var2);
                    break;
                default:
                    pushNum(calc, c);
                }
            }

        } while (true);
    }

    private static void popNum(Stack calc) {
        try {
            System.out.println(calc.pop());
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
        }
    }

    private static void add(Stack calc, int var1, int var2) {

        try {
            var1 = Integer.parseInt(calc.peek());
            calc.pop();
            var2 = Integer.parseInt(calc.peek());
            calc.pop();
            int var3 = var1 + var2;
            calc.push(Integer.toString(var3));
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
            //Technically speaking, if we call this method when nothing is on the stack, 0 will be pushed to the top because that's the default value of var1. There's a better way to do this, but this will work for our purposes.
            calc.push(Integer.toString(var1)); //(see above)
        }
    }

    private static void subtract(Stack calc, int var1, int var2) {
        try {
            var1 = Integer.parseInt(calc.peek());
            calc.pop();
            var2 = Integer.parseInt(calc.peek());
            calc.pop();
            int var3 = var2 - var1;
            calc.push(Integer.toString(var3));
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
            calc.push(Integer.toString(var1)); //Leave stack unchanged
        }
    }

    private static void multiply(Stack calc, int var1, int var2) {
        try {
            var1 = Integer.parseInt(calc.peek());
            calc.pop();
            var2 = Integer.parseInt(calc.peek());
            calc.pop();
            int var3 = var1 * var2;
            calc.push(Integer.toString(var3));
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
            calc.push(Integer.toString(var1)); //Leave stack unchanged
        }
    }

    private static void mod(Stack calc, int var1, int var2) {
        try {
            var1 = Integer.parseInt(calc.peek());
            calc.pop();
            var2 = Integer.parseInt(calc.peek());
            calc.pop();
            int var3 = modByZero(calc, var2, var1);
            calc.push(Integer.toString(var3));
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
            calc.push(Integer.toString(var1)); //Leave stack unchanged
        }
    }

    private static void div(Stack calc, int var1, int var2) {
        try {
            var1 = Integer.parseInt(calc.peek());
            calc.pop();
            var2 = Integer.parseInt(calc.peek());
            calc.pop();
            int var3 = divisionByZero(calc, var2, var1);
            calc.push(Integer.toString(var3));
        } catch (EmptyException e) {
            System.out.println("ERROR: Not enough arguments.");
            calc.push(Integer.toString(var1));
        }
    }

    private static void pushNum(Stack calc, String s) {
        try {
            int intVal = Integer.parseInt(s); //See if s is numerical
            calc.push(s);                     //Push s if so
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: bad token");
        }
    }

    //division by zero exceptions
    private static int divisionByZero(Stack calc, int var1, int var2) {

        try {
            return var1 / var2; //Try expression
        } catch (ArithmeticException e) {
            System.out.println("ERROR: division by zero");
            calc.push(Integer.toString(var1)); //Replace var1 if error
        }
        return var2; //return var2 so it can be replaced in stack
    }

    private static int modByZero(Stack calc, int var1, int var2) {
        try {
            return var1 % var2;
        } catch (ArithmeticException e) {
            System.out.println("ERROR: modulus by zero");
            calc.push(Integer.toString(var1));
        }
        return var2;
    }
}
