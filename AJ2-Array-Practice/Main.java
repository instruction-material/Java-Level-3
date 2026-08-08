class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] words = {"happy", "juni", "computer"};

        System.out.println("\nOriginal array:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println("\n\nEvery number doubled:");
        int[] arr2 = doubleNums(arr);
        for (int i = 0; i < arr2.length; i++) {
            System.out.print(arr2[i] + " ");
        }

        System.out.print("\n\nSum of all even numbers: ");
        System.out.println(sumEvens(arr));

        System.out.println("\nRandom doubles between 15 and 100:");
        double[] arr3 = randomNums(6);
        for (int j = 0; j < arr3.length; j++) {
            System.out.print(arr3[j] + " ");
        }

        System.out.println("\n\nArray of words: ");
        for (int i = 0; i < words.length; i++) {
            System.out.print(words[i] + " ");
        }

        System.out.print("\n\nSum of word lengths: ");
        System.out.println(sumLetters(words));
    }

    // 1. Write a method which takes in an array of numbers and returns an array with each number doubled.
    public static int[] doubleNums(int[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] * 2;
        }
        return result;
    }

    // 2. Write a method which takes in an array of numbers and returns the sum of the even numbers in the array.
    public static int sumEvens(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 0) {
                sum += arr[i];
            }
        }
        return sum;
    }

    // 3. Write a method which takes in a number N and returns an array with N random doubles between 15 and 100.
    public static double[] randomNums(int N) {
        double[] result = new double[N];
        for (int i = 0; i < N; i++) {
            double num = (Math.random() * 85) + 15;
            result[i] = num;
        }
        return result;
    }

    // 4. Write a method which takes in an array of words and returns the sum of all their lengths
    public static int sumLetters(String[] words) {
        int sum = 0;
        for (int i = 0; i < words.length; i++) {
            sum += words[i].length();
        }
        return sum;
    }
}
