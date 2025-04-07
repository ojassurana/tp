package exception;

/**
 * Exception thrown when an index is not specified.
 * This exception extends InvalidIndexException and indicates that the provided index is null or missing.
 */
public class NullIndexException extends InvalidIndexException {
    public static final String DEFAULT_MESSAGE =
            "\tIndex was not specified.";

    /**
     * Constructs a NullIndexException with the default message and additional details from InvalidIndexException.
     */
    public NullIndexException() {
        super(DEFAULT_MESSAGE + InvalidIndexException.DEFAULT_MESSAGE);
    }
}
