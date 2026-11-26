public class Main {
    public static void main(String[] args) {
        // Run this deliberate error separately to see its stack trace.
        if (args.length == 1 && args[0].equals("runtime-error")) {
            runtimeException();
            return;
        }

        handleImmediately();
        try {
            handleLater();
        } catch (CustomException error) {
            System.out.println(error.getMessage());
        }
    }

    private static void handleImmediately() {
        try {
            throw new CustomException("Caught in handleImmediately()");
        } catch (CustomException error) {
            System.out.println(error.getMessage());
        }
    }

    private static void handleLater() throws CustomException {
        throw new CustomException("Caught by the caller of handleLater()");
    }

    private static void runtimeException() {
        int[] values = new int[0];
        values[-1] = 0;
    }
}
