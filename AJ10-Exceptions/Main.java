public class Main {
    public static void main(String args[]) {

        //RUNTIME EXCEPTIONS:
        //A runtime exception is caused by a bug. Most of the time we should fix the bug rather than handle it with a try-catch block
        runtimeException();

        //CHECKED EXCEPTIONS: ------------------------------------------
        //caused by something outside the programmers control (usually when you're working with external resources (file streams, internet, user-input, etc.)

        //1. HANDLE IMMEDIATELY:
        //Notice how the exception is handled in the method
        handleImmediately();

        //2. DECLARE AND HANDLE LATER:
        //Notice how the exception is declared instead of handled in the method, so now we have to handle it here.
        try {
            handleLater();
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //CHECKED EXCEPTIONS ---------------------------------------------
    private static void handleImmediately() {
        try {
            //there's no good reason why we threw this exception, we just did it to show the difference between handling immediately vs declaring and handling elsewhere
            throw new CustomException(
                "ERROR: something went wrong in handleImmediately()");
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private static void handleLater() throws CustomException {
        throw new CustomException(
            "ERROR: something went wrong in handleLater()");
    }

    //RUNTIME EXCEPTION ----------------------------------------------
    private static void runtimeException() {
        int[] array = new int[0];
        //fix this line of code rather than handle with try-catch
        array[-1] = 0;
    }
}
