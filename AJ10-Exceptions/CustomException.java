public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;

    public CustomException(String msg) {
        //call the super class constructor with the custom message
        super(msg);
    }
}
