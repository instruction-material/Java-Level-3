import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 1. Ask the user for a size and create a square two-dimensional array of that size. Store the sum of the row and column indices in each slot.
        System.out.print("\nEnter the size of the array: ");
        final int SIZE = in.nextInt();
        int[][] arr = new int[SIZE][SIZE];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = (i) + (j);
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }

        //Use the method created for question 2
        System.out.println("\nMaximum: " + maxNum(arr));

        //Use the method created for question 3
        System.out.print("\nEnter the number of rows: ");
        int rows = in.nextInt();
        System.out.print("Enter the number of columns: ");
        int columns = in.nextInt();
        System.out.println(
            "\nTwo dimensional array filled with random doubles:");
        double[][] arr2 = doubleArray(rows, columns);
        for (double[] row : arr2) {
            for (double num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }

        //Use the method created for question 4
        System.out.print("\nIs 5 in the original array? ");
        System.out.println(productFinder(arr, 5));
    }

    // 2. Write a method that returns the maximum of a two-dimensionsensional array.
    public static int maxNum(int[][] arr) {
        int maximum = arr[0][0];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] > maximum) {
                    maximum = arr[i][j];
                }
            }
        }
        return maximum;
    }

    // 3. Write a method that takes in two dimensions(row, column) and returns a two dimensional array with those dimensions that contains random doubles between 0 and 10.
    public static double[][] doubleArray(int row, int col) {
        double[][] result = new double[row][col];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = Math.random() * 10;
            }
        }
        return result;
    }

    // 4. Write a method which takes in a two-dimensional square array of numbers and an integer N. The method should return true if N is in the array and false otherwise.
    public static boolean productFinder(int[][] arr, int N) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if (arr[i][j] == N) {
                    return true;
                }
            }
        }
        return false;
    }
}
