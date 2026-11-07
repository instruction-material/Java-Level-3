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
        if (n < 0 || n > 12) {
            throw new IllegalArgumentException("n must be between 0 and 12");
        }
        if (n <= 1) return 1;
        return Math.multiplyExact(n, factorial(n - 1));
    }

    // Accepted p: 0..31. The empty product, including exponent(0, 0), is 1.
    // An exact result outside int range throws ArithmeticException.
    public static int exponent(int b, int p) {
        if (p < 0 || p > 31) {
            throw new IllegalArgumentException("power must be between 0 and 31");
        }
        if (p == 0) return 1;
        return Math.multiplyExact(b, exponent(b, p - 1));
    }

    // Sum the decimal digits of the magnitude of any signed int.
    public static int sumDigits(int n) {
        if (n < 0) {
            long magnitude = -(long) n;
            return (int) (magnitude % 10) + sumDigits((int) (magnitude / 10));
        }
        if (n < 10) return n;
        return n % 10 + sumDigits(n / 10);
    }

    // Preserve the original project's one-based terms: 1 -> 0, 2 -> 1.
    // This introductory branching implementation is bounded to 31 terms.
    public static int fibonacci(int n) {
        if (n < 1 || n > 31) {
            throw new IllegalArgumentException("term must be between 1 and 31");
        }
        if (n == 1) return 0;
        if (n == 2) return 1;
        return fibonacci(n - 2) + fibonacci(n - 1);
    }

    // Case-sensitive ASCII exercise: punctuation and spaces count.
    public static boolean palindrome(String word) {
        if (word == null || word.length() > 256) {
            throw new IllegalArgumentException("word must have at most 256 ASCII characters");
        }
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) > 127) {
                throw new IllegalArgumentException("word must contain only ASCII characters");
            }
        }
        if (word.length() < 2) return true;
        if (word.charAt(0) != word.charAt(word.length() - 1)) return false;
        return palindrome(word.substring(1, word.length() - 1));
    }
}
