package exception;

/**
 * Exception thrown when an invalid index is specified.
 * This exception indicates that the provided index is not valid and does not meet the expected criteria.
 */
public class InvalidIndexException extends Exception {
    public static final String DEFAULT_MESSAGE =
            "\n\tPlease specify a valid Index.";

    /**
     * Constructs an InvalidIndexException with the default message.
     */
    public InvalidIndexException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs an InvalidIndexException with a custom message.
     * @param message The custom message describing the exception.
     */
    public InvalidIndexException(String message) {
        super(message);
    }
}
