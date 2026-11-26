package runner;

import commands.Parser;

public class Main {
    public static void main(String[] args) {
        var parser = new Parser();
        System.out.println(parser.parse("ADD pencils 3"));
        System.out.println(parser.parse("PRINT"));
    }
}
