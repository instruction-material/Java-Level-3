public class Recursion {
    public static void main(String[] args) {

        ///////////////////////////////
        // RECURSION
        ///////////////////////////////

        //Recursion-3: Write a recursive method that returns the length of a string without using the string.length() method.
        String msg = "hello world!";
        System.out.print("\nLength of \"hello world\": ");
        System.out.print(lengthOf(msg));

        //Recursion-4: Write a recursive function that finds the number in Pascal’s triangle at the given row and column.
        System.out.println("\nPascal's Triangle: ");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(pascalsTriangle(i, j) + " ");
            }
            System.out.println();
        }

        //Recursion-5: Referring to the starter code, what will be printed if we call strangeMethod(5)?
        System.out.println("\nStrange Method:");
        strangeMethod(5);
    }
    public static int lengthOf(String str) {
        if (str.compareTo("") == 0) {
            return 0;
        }
        return 1 + lengthOf(str.substring(1));
    }
    public static int pascalsTriangle(int row, int col) {
        if (row == 0 && col == 0) {
            return 1;
        } else if (col < 0 || col > row) {
            return 0;
        }
        return pascalsTriangle(row - 1, col) +
            pascalsTriangle(row - 1, col - 1);
    }
    public static void strangeMethod(int num) {
        System.out.println(num);
        if (num > 2) {
            strangeMethod(num - 1);
            strangeMethod(num - 2);
        }
    }
}
