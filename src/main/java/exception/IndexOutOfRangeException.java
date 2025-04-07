package exception;

/**
 * Exception thrown when an index is out of range.
 * This exception extends InvalidIndexException and specifies that the provided index
 * exceeds the valid range of allowable values.
 */
public class IndexOutOfRangeException extends InvalidIndexException {
    public static final String DEFAULT_MESSAGE =
            "\tIndex is out of range!";

    /**
     * Constructs an IndexOutOfRangeException with the default message and additional details.
     */
    public IndexOutOfRangeException() {
        super(DEFAULT_MESSAGE + InvalidIndexException.DEFAULT_MESSAGE);
    }
}
