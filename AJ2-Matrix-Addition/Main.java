import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        //get the number of rows and columns
        System.out.print("\nEnter the number of rows: ");
        int row = in.nextInt();
        System.out.print("Enter the number of columns: ");
        int col = in.nextInt();

        //create the two two-dimensional arrays
        System.out.println("\nEntering data for matrix #1...");
        int[][] mat1 = enterData(row, col);
        System.out.println("\nEntering data for matrix #2...");
        int[][] mat2 = enterData(row, col);

        //add the two arrays together
        int[][] result = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result[i][j] = mat1[i][j] + mat2[i][j];
            }
        }

        //print the result
        System.out.println("\nResulting matrix: ");
        display(result);
    }

    //fills a two-dimentional array with the given number of rows and columns
    public static int[][] enterData(int row, int col) {
        Scanner in = new Scanner(System.in);
        int[][] result = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print("Element in row " + (i + 1) + " and column " +
                                 (j + 1) + ": ");
                result[i][j] = in.nextInt();
            }
        }
        return result;
    }

    //prints out a two dimensional array
    public static void display(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
}
