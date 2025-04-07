package exception;

public class InvalidIndexException extends Exception {
    public static final String DEFAULT_MESSAGE =
            "\n\tPlease specify a valid Index." ;
    public InvalidIndexException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidIndexException(String message) {
        super(message);
    }
}
