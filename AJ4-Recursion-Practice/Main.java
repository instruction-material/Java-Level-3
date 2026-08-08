class Main {
    public static void main(String[] args) {
        System.out.print("\nFactorial of 5: ");
        System.out.println(factorial(5));
        System.out.print("2^3: ");
        System.out.println(exponent(2, 3));
        System.out.print("Sum of the digits 1234: ");
        System.out.println(sumDigits(1234));
        System.out.print("5th Fibonacci Number: ");
        System.out.println(fibonacci(5));
        System.out.print("Is \"racecar\" a palindrome? ");
        System.out.println(palindrome("racecar"));
    }

    // 1. Write a recursive method that takes in a number and returns the factorial of the number.
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    // 2. Write a recursive method that takes in base and power and returns the value of the exponent.
    public static int exponent(int b, int p) {
        if (p == 0) {
            return 1;
        }
        return b * exponent(b, p - 1);
    }

    // 3. Write a recursive function that takes in a number and returns the sum of the digits in that number. Ask the student to first figure out how to write a function to get the value of the ones digit (hint: need to use mod). Then, figure out how to write a function to get the value of the tens digit.
    public static int sumDigits(int n) {
        int mod = n % 10;
        if (mod == n) {
            return n;
        }

        // Note: Java will cast (n/10) to an integer, so we don't have to write ((n-mod)/10)
        return mod + sumDigits(n / 10);
    }

    // 4. Write a recursive function that takes in a number n and returns the nth Fibonacci number. Remember, the Fibonacci sequence is 0, 1, 1, 2, 3, 5, 8...
    public static int fibonacci(int n) {
        if (n == 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        return fibonacci(n - 2) + fibonacci(n - 1);
    }

    // 5. Write a recursive function that takes in a word returns True if the word is a palindrome, otherwise return False. This is also good practice with the String methods!
    public static boolean palindrome(String word) {
        if (word.length() == 0 || word.length() == 1) {
            return true;
        } else if (word.charAt(0) != word.charAt(word.length() - 1)) {
            return false;
        }
        return palindrome(word.substring(1, word.length() - 1));
    }
}
