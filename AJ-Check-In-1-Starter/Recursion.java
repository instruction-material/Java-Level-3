public class Recursion {
    public static void main(String[] args) {

        ///////////////////////////////
        // RECURSION
        ///////////////////////////////

        //Recursion-5: Referring to the starter code, what will be printed if we call strangeMethod(5)?
    }
    public static void strangeMethod(int num) {
        System.out.println(num);
        if (num > 2) {
            strangeMethod(num - 1);
            strangeMethod(num - 2);
        }
    }
}
