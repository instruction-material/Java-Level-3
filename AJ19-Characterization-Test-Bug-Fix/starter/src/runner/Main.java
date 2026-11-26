package runner;

import quotes.Shipping;

public final class Main {
    public static void main(String[] args) {
        System.out.println(new Shipping().charge(5_000) + " cents");
    }
}
