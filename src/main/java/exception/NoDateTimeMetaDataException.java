package exception;

/**
 * Exception thrown when the uploaded photo has no DateTime metadata.
 * This exception extends NoMetaDataException and specifies that the missing metadata
 * pertains to the DateTime information of the photo file.
 */
public class NoDateTimeMetaDataException extends NoMetaDataException {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no DateTime metadata.";

    /**
     * Constructs a NoDateTimeMetaDataException with the default message and guide.
     */
    public NoDateTimeMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
