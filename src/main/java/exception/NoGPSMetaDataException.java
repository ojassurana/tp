package exception;

/**
 * Exception thrown when the uploaded photo has no GPS metadata.
 * This exception extends NoMetaDataException and specifies that the missing metadata
 * pertains to the GPS location information of the photo file.
 */
public class NoGPSMetaDataException extends NoMetaDataException {
    public static final String DEFAULT_MESSAGE =
            "\n\tThe photo uploaded has no GPS metadata.";

    /**
     * Constructs a NoGPSMetaDataException with the default message and guide.
     */
    public NoGPSMetaDataException() {
        super(DEFAULT_MESSAGE + GUIDE);
    }
}
