class Main {
    public static void main(String[] args) {
        System.out.println("Factorial of 5: " + factorial(5));
        System.out.println("2^3: " + exponent(2, 3));
        System.out.println("Sum of the digits 1234: " + sumDigits(1234));
        System.out.println("5th Fibonacci number (term 1 is 0): " + fibonacci(5));
        System.out.println("Is \"racecar\" a palindrome? " + palindrome("racecar"));
    }

    // Accepted n: 0..12. Invalid inputs throw IllegalArgumentException.
    public static int factorial(int n) {
        throw new UnsupportedOperationException("Implement factorial");
    }

    // Accepted p: 0..31. The empty product, including exponent(0, 0), is 1.
    // An exact result outside int range throws ArithmeticException.
    public static int exponent(int b, int p) {
        throw new UnsupportedOperationException("Implement exponent");
    }

    // Sum the decimal digits of the magnitude of any signed int.
    public static int sumDigits(int n) {
        throw new UnsupportedOperationException("Implement sumDigits");
    }

    // Preserve the original project's one-based terms: 1 -> 0, 2 -> 1.
    // This introductory branching implementation is bounded to 31 terms.
    public static int fibonacci(int n) {
        throw new UnsupportedOperationException("Implement fibonacci");
    }

    // Case-sensitive ASCII exercise: punctuation and spaces count.
    public static boolean palindrome(String word) {
        throw new UnsupportedOperationException("Implement palindrome");
    }
}
