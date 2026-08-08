import java.util.*;

class Main {
    public static void main(String[] args) {

        ArrayList<String> expressions = new ArrayList<>();

        expressions.add("()");
        expressions.add("[](){}");
        expressions.add("{()[]}");
        expressions.add("]");
        expressions.add("{()[}]");
        expressions.add("{(((())))[}]");
        expressions.add("{}/");

        System.out.println();
        for (String e : expressions) {
            System.out.print(e + " --> ");
            System.out.println(parentheses(e));
        }
    }

    public static boolean parentheses(String brackets) {

        char[] open = {'{', '(', '['};
        char[] close = {'}', ')', ']'};
        Stack s = new Stack();

        if (brackets.length() == 0) {
            //VALID EXPRESSION!
            //correct because there's nothing there
            return true;
        }

        for (int i = 0; i < brackets.length(); i++) {
            if (inArray(open, brackets.charAt(i))) {
                //push the open symbol
                s.push(brackets.charAt(i));
            } else if (inArray(close, brackets.charAt(i))) {
                //pop with the close symbol if possible

                if (s.isEmpty()) {
                    //invalid expression
                    //there were more closing than opening
                    return false;
                } else {

                    char p = s.peek();
                    int j = findElement(open, p);
                    if (brackets.charAt(i) != close[j]) {
                        //invalid expression
                        //opening and closing symbols don't correspond
                        return false;
                    } else {
                        s.pop();
                    }
                }
            } else {
                //invalid expression
                //invalid character
                return false;
            }
        }

        if (s.isEmpty()) {
            //VALID EXPRESSION!
            return true;
        } else {
            //invalid expression
            //there were more opening than closing
            return false;
        }
    }

    //returns whether the given symbol...
    //is in the array (containing all the possible symbols)
    public static boolean inArray(char[] arr, char query) {
        for (char i : arr) {
            if (i == query) {
                return true;
            }
        }
        return false;
    }

    //returns the index of the symbol in one array (such as opening)
    //so that it can be used to look up the corresponding symbol
    //in the other array (such as closing)
    public static int findElement(char[] arr, char query) {
        for (int i = 0; i < arr.length; i++) {
            if (query == arr[i]) {
                return i;
            }
        }
        return -1;
    }
}
